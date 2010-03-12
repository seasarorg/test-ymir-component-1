package org.seasar.ymir.impl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.cms.pluggable.ThreadContext;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Action;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.ActionNotFoundRuntimeException;
import org.seasar.ymir.AttributeContainer;
import org.seasar.ymir.ComponentMetaDataFactory;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.ExceptionProcessor;
import org.seasar.ymir.FrameworkDispatch;
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
import org.seasar.ymir.Ymir;
import org.seasar.ymir.annotation.Include;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;
import org.seasar.ymir.response.PassthroughResponse;
import org.seasar.ymir.util.RequestUtils;
import org.seasar.ymir.util.YmirUtils;

public class RequestProcessorImpl implements RequestProcessor {
    static final Set<ConstraintType> EMPTY_SUPPRESSTYPESET = EnumSet
            .noneOf(ConstraintType.class);

    private Ymir ymir_;

    private ActionManager actionManager_;

    private AnnotationHandler annotationHandler_;

    private ComponentMetaDataFactory componentMetaDataFactory_;

    private ExceptionProcessor exceptionProcessor_;

    private Updater[] updaters_ = new Updater[0];

    private YmirProcessInterceptor[] ymirProcessInterceptors_ = new YmirProcessInterceptor[0];

    private PageComponentVisitor<Response> visitorForInvoking_;

    private final Log log_ = LogFactory.getLog(RequestProcessorImpl.class);

    @Binding(bindingType = BindingType.MUST)
    public void setYmir(Ymir ymir) {
        ymir_ = ymir;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setActionManager(ActionManager actionManager) {
        actionManager_ = actionManager;
        initPageComponentVisitor();
    }

    @Binding(bindingType = BindingType.MUST)
    public void setAnnotationHandler(AnnotationHandler annotationHandler) {
        annotationHandler_ = annotationHandler;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setComponentMetaDataFactory(
            ComponentMetaDataFactory componentMetaDataFactory) {
        componentMetaDataFactory_ = componentMetaDataFactory;
        initPageComponentVisitor();
    }

    @Binding(bindingType = BindingType.MUST)
    public void setExceptionProcessor(ExceptionProcessor exceptionProcessor) {
        exceptionProcessor_ = exceptionProcessor;
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

    void initPageComponentVisitor() {
        if (actionManager_ == null || componentMetaDataFactory_ == null) {
            return;
        }

        visitorForInvoking_ = new VisitorForInvoking(actionManager_,
                componentMetaDataFactory_);
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
        if (RequestUtils.isProceeded(request)) {
            // proceedはredirectと同じように扱う。
            // そのためdispatcherがrequestである場合と同じ処理を行なう。
            return processRequest(request);
        } else {
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
    }

    protected Response processRequest(final Request request) {
        Dispatch dispatch = request.getCurrentDispatch();
        if (dispatch.isDenied()) {
            throw new PageNotFoundRuntimeException(dispatch.getPath());
        }

        boolean updatable = YmirUtils.isUpdatable(request);

        // 自動生成はrequestの時だけ行なう。（forwardの自動生成はどのみちここでキャッチされて
        // 処理されてしまうので…。）
        if (ymir_.isUnderDevelopment() && updatable) {
            for (int i = 0; i < updaters_.length; i++) {
                Response response = updaters_[i].updateByRequesting(request);
                if (response != null) {
                    return response;
                }
            }
        }

        Response response = processRequestAndForward(request);

        // 自動生成はrequestの時だけ行なう。（forwardの自動生成はどのみちここでキャッチされて
        // 処理されてしまうので…。）
        if (ymir_.isUnderDevelopment() && updatable) {
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
        Object page = null;

        if (!dispatch.isIgnored()) {
            PageComponent pageComponent = createPageComponent(dispatch
                    .getPageComponentName());

            // dispatch.isMatched()がtrueの場合でもpageComponentがnullになることがある
            // ことに注意。
            // 具体的には、page名が割り当てられているのにまだ対応するPageクラスが作成されていない場合、
            // またはDeniedPathMappingImplに関して処理をしている場合にnullになる。
            if (pageComponent != null) {
                do {
                    try {
                        dispatch.setPageComponent(pageComponent);

                        // PageComponent作成直後に、
                        // 先にリクエストに対応するアクションを決定しておく。
                        // Invoke処理をactionName毎に切り替えられるようにした場合
                        // の布石。
                        final Action originalAction = dispatch
                                .getMatchedPathMapping().getAction(
                                        pageComponent, request);
                        dispatch.setOriginalAction(originalAction);
                        dispatch.setAction(originalAction);

                        Response r = pageComponent.accept(visitorForInvoking_,
                                Phase.PAGECOMPONENT_CREATED, dispatch
                                        .getActionName());
                        if (r.getType() != ResponseType.PASSTHROUGH) {
                            response = r;
                            break;
                        }

                        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
                            pageComponent = ymirProcessInterceptors_[i]
                                    .pageComponentCreated(request,
                                            pageComponent);
                        }

                        // 実際のアクションを決定する。
                        Action action = originalAction;
                        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
                            action = ymirProcessInterceptors_[i]
                                    .actionInvoking(request, action);
                            dispatch.setAction(action);
                        }
                        String actionName;
                        if (action != null) {
                            page = action.getTarget();
                            actionName = action.getName();
                        } else {
                            page = null;
                            actionName = null;
                        }

                        if (action == null
                                && dispatch.getDispatcher() == Dispatcher.REQUEST) {
                            // リクエストに対応するアクションが存在しない場合はリクエストを受け付けない。
                            // ただしforwardの時はアクションがなくても良いことにしている。
                            // これは、forward先のパスに対応するPageクラスでは_prerender()だけ
                            // 呼びたい場合にアクションメソッドを省略できるようにするため。
                            throw new ActionNotFoundRuntimeException(dispatch
                                    .getPath(), request.getMethod());
                        }

                        r = pageComponent.accept(visitorForInvoking_,
                                Phase.ACTION_INVOKING, actionName);
                        if (r.getType() != ResponseType.PASSTHROUGH) {
                            response = r;
                            break;
                        }

                        response = actionManager_.invokeAction(action);

                        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
                            response = ymirProcessInterceptors_[i]
                                    .actionInvoked(request, response);
                        }

                        r = pageComponent.accept(visitorForInvoking_,
                                Phase.ACTION_INVOKED, actionName);
                        if (r.getType() != ResponseType.PASSTHROUGH) {
                            response = r;
                            break;
                        }
                    } catch (Throwable t) {
                        response = exceptionProcessor_
                                .process(request, t, true);
                    }
                } while (false);

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
            }

            if (log_.isDebugEnabled()) {
                log_.debug("Raw response: " + response);
            }

            // pageComponentがnullの場合でも、自動生成機能でクラスやテンプレートの自動生成が
            // 適切にできるようにデフォルト値からResponseを作るようにする。
            // （例えば、リクエストパス名がテンプレートパス名ではない場合に、リクエストパス名で
            // テンプレートが作られてしまうとうれしくない。）
            response = adjustResponse(dispatch, response, page);

            if (log_.isDebugEnabled()) {
                log_.debug("Adjusted response: " + response);
            }
        }

        return response;
    }

    protected Response processInclude(final Request request) {
        Response response = new PassthroughResponse();

        Dispatch dispatch = request.getCurrentDispatch();
        if (!dispatch.isIgnored()) {
            // includeの場合はselfを設定するだけ。
            Object page = getPage(request.getCurrentDispatch()
                    .getPageComponentName());
            if (page != null) {
                request.setAttribute(ATTR_SELF, page);
            }
            response = adjustResponse(dispatch, response, page);
        }

        return response;
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

    Response adjustResponse(Dispatch dispatch, Response response, Object page) {
        if (response.getType() == ResponseType.PASSTHROUGH
                && !fileResourceExists(dispatch.getPath())) {
            Object returnValue = dispatch.getMatchedPathMapping()
                    .getDefaultReturnValue();
            if (returnValue != null) {
                response = actionManager_.constructResponse(page, Object.class,
                        returnValue);
            }
        }

        return response;
    }

    ThreadContext getThreadContext() {
        return (ThreadContext) ymir_.getApplication().getS2Container()
                .getRoot().getComponent(ThreadContext.class);
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

    protected class VisitorForPrerendering extends PageComponentVisitor<Object> {
        private Request request_;

        private MatchedPathMapping matched_;

        public VisitorForPrerendering(Request request) {
            request_ = request;
            matched_ = request_.getCurrentDispatch().getMatchedPathMapping();
        }

        public Object process(PageComponent pageComponent, Object... parameters) {
            actionManager_.invokeAction(matched_.getPrerenderAction(
                    pageComponent, request_));
            return null;
        }
    }
}