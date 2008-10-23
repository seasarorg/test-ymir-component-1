package org.seasar.ymir.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Action;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.ActionNotFoundRuntimeException;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.AttributeContainer;
import org.seasar.ymir.ComponentMetaData;
import org.seasar.ymir.ComponentMetaDataFactory;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.FrameworkDispatch;
import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageComponentVisitor;
import org.seasar.ymir.PageNotFoundRuntimeException;
import org.seasar.ymir.Phase;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.Updater;
import org.seasar.ymir.WrappingRuntimeException;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.annotation.Include;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;
import org.seasar.ymir.response.PassthroughResponse;
import org.seasar.ymir.response.constructor.ResponseConstructor;
import org.seasar.ymir.response.constructor.ResponseConstructorSelector;
import org.seasar.ymir.util.YmirUtils;

public class RequestProcessorImpl implements RequestProcessor {
    static final Set<ConstraintType> EMPTY_SUPPRESSTYPESET = EnumSet
            .noneOf(ConstraintType.class);

    private Ymir ymir_;

    private ActionManager actionManager_;

    private AnnotationHandler annotationHandler_;

    private ComponentMetaDataFactory componentMetaDataFactory_;

    private ResponseConstructorSelector responseConstructorSelector_;

    private Updater[] updaters_ = new Updater[0];

    private YmirProcessInterceptor[] ymirProcessInterceptors_ = new YmirProcessInterceptor[0];

    private final PageComponentVisitor<Object> visitorForInvokingInPhasePageComponentCreated_ = new VisitorForInvoking(
            Phase.PAGECOMPONENT_CREATED);

    private final PageComponentVisitor<Object> visitorForInvokingInPhaseActionInvoking_ = new VisitorForInvoking(
            Phase.ACTION_INVOKING);

    private final PageComponentVisitor<Object> visitorForInvokingInPhaseActionInvoked_ = new VisitorForInvoking(
            Phase.ACTION_INVOKED);

    private final Log log_ = LogFactory.getLog(RequestProcessorImpl.class);

    @Binding(bindingType = BindingType.MUST)
    public void setYmir(Ymir ymir) {
        ymir_ = ymir;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setActionManager(ActionManager actionManager) {
        actionManager_ = actionManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setAnnotationHandler(AnnotationHandler annotationHandler) {
        annotationHandler_ = annotationHandler;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setComponentMetaDataFactory(
            ComponentMetaDataFactory componentMetaDataFactory) {
        componentMetaDataFactory_ = componentMetaDataFactory;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setResponseConstructorSelector(
            ResponseConstructorSelector responseConstructorSelector) {
        responseConstructorSelector_ = responseConstructorSelector;
    }

    public void setUpdaters(Updater[] updaters) {
        updaters_ = updaters;
    }

    @Binding(value = "@org.seasar.ymir.util.ContainerUtils@findAllComponents(container, @org.seasar.ymir.interceptor.YmirProcessInterceptor@class)", bindingType = BindingType.MUST)
    public void setYmirProcessInterceptors(
            YmirProcessInterceptor[] ymirProcessInterceptors) {
        ymirProcessInterceptors_ = ymirProcessInterceptors;
        YmirUtils.sortYmirProcessInterceptors(ymirProcessInterceptors_);
    }

    String strip(String path) {
        int question = path.indexOf('?');
        if (question >= 0) {
            path = path.substring(0, question);
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    public Object backupForInclusion(AttributeContainer attributeContainer) {
        return attributeContainer.getAttribute(ATTR_SELF);
    }

    public void restoreForInclusion(AttributeContainer attributeContainer,
            Object backupped) {
        attributeContainer.setAttribute(ATTR_SELF, backupped);
    }

    ServletContext getServletContext() {
        return (ServletContext) getS2Container().getRoot().getComponent(
                ServletContext.class);
    }

    protected S2Container getS2Container() {
        return ymir_.getApplication().getS2Container();
    }

    public Response process(final Request request) {
        switch (request.getCurrentDispatch().getDispatcher()) {
        case REQUEST:
            return processRequest(request);

        case FORWARD:
            return processForward(request);

        case INCLUDE:
            return processInclude(request);

        default:
            return new PassthroughResponse();
        }
    }

    protected Response processRequest(final Request request) {
        Dispatch dispatch = request.getCurrentDispatch();
        if (dispatch.isDenied()) {
            throw new PageNotFoundRuntimeException(dispatch.getPath());
        }

        Response response = processRequestAndForward(request);

        // 自動生成はrequestの時だけ行なう。（forwardの自動生成はどのみちここでキャッチされて
        // 処理されてしまうので…。）
        if (ymir_.isUnderDevelopment()) {
            for (int i = 0; i < updaters_.length; i++) {
                Response newResponse = updaters_[i].update(request, response);
                if (newResponse != response) {
                    return newResponse;
                }
            }
        }

        return response;
    }

    protected Response processForward(final Request request) {
        return processRequestAndForward(request);
    }

    protected Response processRequestAndForward(final Request request) {
        FrameworkDispatch dispatch = YmirUtils.toFrameworkDispatch(request
                .getCurrentDispatch());

        Response response = new PassthroughResponse();

        if (dispatch.isMatched()) {
            PageComponent pageComponent = createPageComponent(dispatch
                    .getPageComponentName());
            if (pageComponent != null) {
                dispatch.setPageComponent(pageComponent);

                pageComponent
                        .accept(visitorForInvokingInPhasePageComponentCreated_);

                for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
                    pageComponent = ymirProcessInterceptors_[i]
                            .pageComponentCreated(request, pageComponent);
                }

                // リクエストに対応するアクションを決定する。

                Action originalAction = dispatch.getMatchedPathMapping()
                        .getAction(pageComponent, request);
                dispatch.setAction(originalAction);

                Action action = originalAction;
                for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
                    action = ymirProcessInterceptors_[i].actionInvoking(
                            request, originalAction, action);
                    dispatch.setAction(action);
                }

                if (action == null
                        && dispatch.getDispatcher() == Dispatcher.REQUEST) {
                    // リクエストに対応するアクションが存在しない場合はリクエストを受け付けない。
                    // ただしforwardの時はアクションがなくても良いことにしている。
                    // これは、forward先のパスに対応するPageクラスでは_prerender()だけ
                    // 呼びたい場合にアクションメソッドを省略できるようにするため。
                    throw new ActionNotFoundRuntimeException(
                            dispatch.getPath(), request.getMethod());
                }

                pageComponent.accept(visitorForInvokingInPhaseActionInvoking_);

                response = adjustResponse(dispatch, invokeAction(action),
                        action != null ? action.getTarget() : null);

                pageComponent.accept(visitorForInvokingInPhaseActionInvoked_);

                // 画面描画のためのAction呼び出しを行なう。
                //
                // 以前はforwardの時に遷移先のページに対応するPageコンポーネントが
                // 存在しない場合は遷移元のページの_prerenderを呼ぶようにしていたが、
                // その場合は通常と表示するテンプレートが異なるのに単一の_prerender
                // で処理しないといけなくなり_prerenderが複雑化するため、
                // こういうケースではアクションメソッド内でforward先のテンプレートのための
                // 準備を行なうようにする方が良い。そのため_prerenderは呼ばないように
                // 変更した。
                if (response.getType() == ResponseType.PASSTHROUGH) {
                    pageComponent.accept(new VisitorForPrerendering(request));
                }

                // Pageコンポーネントをattributeとしてバインドしておく。
                request.setAttribute(ATTR_PAGECOMPONENT, pageComponent);
                request.setAttribute(ATTR_SELF, pageComponent.getPage());
            } else if (dispatch.getPageComponentName() != null) {
                // pageComponentがnullになるのは、page名が割り当てられているのにまだ
                // 対応するPageクラスが作成されていない場合、またはDeniedPathMappingImpl
                // に関して処理をしている場合。
                // 前者の場合自動生成機能でクラスやテンプレートの自動生成が適切にできるように、
                // デフォルト値からResponseを作るようにしている。
                // （例えば、リクエストパス名がテンプレートパス名ではない場合に、リクエストパス名で
                // テンプレートが作られてしまうとうれしくない。）
                response = adjustResponse(dispatch, response, null);
            }

            if (log_.isDebugEnabled()) {
                log_.debug("FINAL RESPONSE: " + response);
            }
        }

        return response;
    }

    protected Response processInclude(final Request request) {
        Dispatch dispatch = request.getCurrentDispatch();
        if (dispatch.isMatched()) {
            // includeの場合はselfを設定するだけ。
            Object page = getPage(request.getCurrentDispatch()
                    .getPageComponentName());
            if (page != null) {
                request.setAttribute(ATTR_SELF, page);
            }
        }

        return new PassthroughResponse();
    }

    protected PageComponent createPageComponent(Object pageComponentKey) {
        return createPageComponent(getPage(pageComponentKey),
                getComponentClass(pageComponentKey));
    }

    protected PageComponent createPageComponent(Object page, Class<?> pageClass) {
        if (page == null) {
            return null;
        }

        PageComponent pageComponent;
        Include children = annotationHandler_.getAnnotation(pageClass,
                Include.class);
        if (children != null) {
            Class<?>[] childrenClasses = children.value();
            List<PageComponent> childPageList = new ArrayList<PageComponent>();
            for (int i = 0; i < childrenClasses.length; i++) {
                PageComponent pc = createPageComponent(childrenClasses[i]);
                if (pc != null) {
                    childPageList.add(pc);
                }
            }
            pageComponent = new PageComponentImpl(page, pageClass,
                    childPageList.toArray(new PageComponent[0]));
        } else {
            pageComponent = new PageComponentImpl(page, pageClass);
        }

        return pageComponent;
    }

    protected Object getPage(Object pageComponentKey) {
        if (pageComponentKey == null) {
            // 主にDeniedPathMappingImplのため。
            return null;
        }

        S2Container s2container = getS2Container();
        if (s2container.hasComponentDef(pageComponentKey)) {
            return s2container.getComponent(pageComponentKey);
        } else {
            return null;
        }
    }

    protected Class<?> getComponentClass(Object componentKey) {
        if (componentKey == null) {
            return null;
        }

        S2Container s2container = getS2Container();
        if (s2container.hasComponentDef(componentKey)) {
            return s2container.getComponentDef(componentKey)
                    .getComponentClass();
        } else {
            return null;
        }
    }

    protected boolean componentExists(Object componentKey) {
        if (componentKey == null) {
            return false;
        }
        return getS2Container().hasComponentDef(componentKey);
    }

    Object getRequestComponent(Request request) {
        return request.getAttribute(ATTR_SELF);
    }

    Response invokeAction(final Action action) {
        Response response = new PassthroughResponse();

        if (action != null && action.shouldInvoke()) {
            if (log_.isDebugEnabled()) {
                log_.debug("INVOKE: " + action.getTarget().getClass() + "#"
                        + action.getMethodInvoker());
            }
            response = constructResponse(action.getTarget(), action
                    .getReturnType(), action.invoke());
            if (log_.isDebugEnabled()) {
                log_.debug("RESPONSE: " + response);
            }
        }

        return response;
    }

    Response adjustResponse(Dispatch dispatch, Response response, Object page) {
        if (response.getType() == ResponseType.PASSTHROUGH
                && !fileResourceExists(dispatch.getPath())) {
            Object returnValue = dispatch.getMatchedPathMapping()
                    .getDefaultReturnValue();
            if (returnValue != null) {
                response = constructResponse(page, Object.class, returnValue);
            }
        }

        if (log_.isDebugEnabled()) {
            log_.debug("[4]RESPONSE: " + response);
        }

        return response;
    }

    @SuppressWarnings("unchecked")
    boolean fileResourceExists(String path) {
        if (path.length() == 0 || path.endsWith("/")) {
            return false;
        }

        Set pathSet = getServletContext().getResourcePaths(
                path.substring(0, path.lastIndexOf('/') + 1));
        if (pathSet == null) {
            // 親ディレクトリがないので子もないはず。
            return false;
        } else {
            // ServletContext.getResource(String)だとディレクトリかどうか分からない。
            // 例えば /aaa/bbb というディレクトリがある場合、Tomcat5.5.26では
            // getResource("/aaa/bbb")がnon null値を返す。
            // このメソッドでは指定されたpathに対応するリソースが存在してかつそれがファイル（＝ディレクトリではない）
            // である場合にtrueを返したいので、getResource(String)ではまずい。
            return pathSet.contains(path);
        }
    }

    @SuppressWarnings("unchecked")
    Response constructResponse(Object page, Class<?> returnType,
            Object returnValue) {
        ResponseConstructor<?> constructor = responseConstructorSelector_
                .getResponseConstructor(returnType);
        if (constructor == null) {
            throw new ComponentNotFoundRuntimeException(
                    "Can't find ResponseConstructor for type '" + returnType
                            + "' in ResponseConstructorSelector");
        }

        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            if (page != null) {
                // XXX request.getComponentClass().getClassLoader()にすべきか？
                Thread.currentThread().setContextClassLoader(
                        page.getClass().getClassLoader());
            }
            return ((ResponseConstructor<Object>) constructor)
                    .constructResponse(page, returnValue);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    protected class VisitorForInvoking extends PageComponentVisitor<Object> {
        private Phase phase_;

        public VisitorForInvoking(Phase phase) {
            phase_ = phase;
        }

        public Object process(PageComponent pageComponent) {
            ComponentMetaData metaData = componentMetaDataFactory_
                    .getInstance(pageComponent.getPageClass());
            Method[] methods = metaData.getMethods(phase_);
            if (methods != null) {
                for (int i = 0; i < methods.length; i++) {
                    actionManager_.newAction(
                            pageComponent.getPage(),
                            actionManager_.newMethodInvoker(pageComponent
                                    .getPageClass(), methods[i])).invoke();
                }
            }
            return null;
        }
    }

    protected class VisitorForPrerendering extends PageComponentVisitor<Object> {
        private Request request_;

        private MatchedPathMapping matched_;

        public VisitorForPrerendering(Request request) {
            request_ = request;
            matched_ = request_.getCurrentDispatch().getMatchedPathMapping();
        }

        public Object process(PageComponent pageComponent) {
            invokeAction(matched_.getPrerenderAction(pageComponent, request_));
            return null;
        }
    }
}