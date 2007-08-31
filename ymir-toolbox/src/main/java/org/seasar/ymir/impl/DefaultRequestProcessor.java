package org.seasar.ymir.impl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.log.Logger;
import org.seasar.ymir.Action;
import org.seasar.ymir.AttributeContainer;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageComponentVisitor;
import org.seasar.ymir.PageNotFoundException;
import org.seasar.ymir.PageProcessor;
import org.seasar.ymir.PagePropertyMetaData;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.Updater;
import org.seasar.ymir.WrappingRuntimeException;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.annotation.Include;
import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;
import org.seasar.ymir.response.PassthroughResponse;
import org.seasar.ymir.response.constructor.ResponseConstructor;
import org.seasar.ymir.response.constructor.ResponseConstructorSelector;
import org.seasar.ymir.util.MethodUtils;
import org.seasar.ymir.util.YmirUtils;

public class DefaultRequestProcessor implements RequestProcessor {
    static final Set<ConstraintType> EMPTY_SUPPRESSTYPESET = EnumSet
            .noneOf(ConstraintType.class);

    private Ymir ymir_;

    private PageProcessor pageProcessor_;

    private ResponseConstructorSelector responseConstructorSelector_;

    private Updater[] updaters_ = new Updater[0];

    private YmirProcessInterceptor[] ymirProcessInterceptors_ = new YmirProcessInterceptor[0];

    private final Logger logger_ = Logger.getLogger(getClass());

    private PageComponentVisitor visitorForRendering_ = new VisitorForRendering();

    @Binding(bindingType = BindingType.MUST)
    public void setYmir(Ymir ymir) {
        ymir_ = ymir;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setPageProcessor(PageProcessor pageProcessor) {
        pageProcessor_ = pageProcessor;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setResponseConstructorSelector(
            ResponseConstructorSelector responseConstructorSelector) {
        responseConstructorSelector_ = responseConstructorSelector;
    }

    public void setUpdaters(Updater[] updaters) {
        updaters_ = updaters;
    }

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

    public Response process(final Request request)
            throws PageNotFoundException, PermissionDeniedException {
        switch (request.getCurrentDispatch().getDispatcher()) {
        case REQUEST:
            return processRequest(request);

        case FORWARD:
            return processForward(request);

        case INCLUDE:
            return processInclude(request);

        default:
            return PassthroughResponse.INSTANCE;
        }
    }

    protected Response processRequest(final Request request)
            throws PageNotFoundException, PermissionDeniedException {
        Dispatch dispatch = request.getCurrentDispatch();
        if (dispatch.isDenied()) {
            throw new PageNotFoundException(dispatch.getPath());
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

    protected Response processForward(final Request request)
            throws PageNotFoundException, PermissionDeniedException {
        return processRequestAndForward(request);
    }

    protected Response processRequestAndForward(final Request request)
            throws PageNotFoundException, PermissionDeniedException {
        Dispatch dispatch = request.getCurrentDispatch();

        Response response = PassthroughResponse.INSTANCE;

        if (dispatch.isMatched()) {
            PageComponent pageComponent = createPageComponent(dispatch
                    .getPageComponentName());
            if (pageComponent != null) {
                dispatch.setPageComponent(pageComponent);

                for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
                    pageComponent = ymirProcessInterceptors_[i]
                            .pageComponentCreated(request, pageComponent);
                }

                // リクエストに対応するアクションを決定する。
                Action action = dispatch.getMatchedPathMapping().getAction(
                        pageComponent, request);
                if (action == null
                        && dispatch.getDispatcher() == Dispatcher.REQUEST) {
                    // リクエストに対応するアクションが存在しない場合はリクエストを受け付けない。
                    // ただしforwardの時はアクションがなくても良いことにしている。
                    // これは、forward先のパスに対応するPageクラスでは_render()だけ
                    // 呼びたい場合にアクションメソッドを省略できるようにするため。
                    throw new PermissionDeniedException("Action not found");
                }
                dispatch.setAction(action);

                pageComponent.accept(new VisitorForPreparing(request));

                Action actualAction = action;
                for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
                    actualAction = ymirProcessInterceptors_[i].actionInvoking(
                            request, action, actualAction);
                }

                response = normalizeResponse(adjustResponse(dispatch,
                        invokeAction(actualAction), pageComponent), dispatch
                        .getPath());

                ResponseType responseType = response.getType();
                if (responseType == ResponseType.PASSTHROUGH
                        || responseType == ResponseType.FORWARD) {
                    // 画面描画のためのAction呼び出しを行なう。
                    pageComponent.accept(visitorForRendering_);
                }

                pageComponent.accept(new VisitorForFinishing(dispatch));

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
                response = normalizeResponse(constructDefaultResponse(dispatch,
                        null), dispatch.getPath());
            }
        }

        if (logger_.isDebugEnabled()) {
            logger_.debug("FINAL RESPONSE: " + response);
        }

        return response;
    }

    protected Response processInclude(final Request request)
            throws PageNotFoundException, PermissionDeniedException {
        // includeの場合はselfを設定するだけ。
        Object page = getPage(request.getCurrentDispatch()
                .getPageComponentName());
        if (page != null) {
            request.setAttribute(ATTR_SELF, page);
        }

        return PassthroughResponse.INSTANCE;
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
        Include children = pageClass.getAnnotation(Include.class);
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
        S2Container s2container = getS2Container();
        if (s2container.hasComponentDef(componentKey)) {
            return s2container.getComponentDef(componentKey)
                    .getComponentClass();
        } else {
            return null;
        }
    }

    Response normalizeResponse(Response response, String path) {
        if (response.getType() == ResponseType.FORWARD
                && response.getPath().equals(path)) {
            return PassthroughResponse.INSTANCE;
        } else {
            return response;
        }
    }

    Object getRequestComponent(Request request) {
        return request.getAttribute(ATTR_SELF);
    }

    Response invokeAction(final Action action) throws PermissionDeniedException {
        Response response = PassthroughResponse.INSTANCE;

        if (action != null && action.shouldInvoke()) {
            if (logger_.isDebugEnabled()) {
                logger_.debug("INVOKE: " + action.getTarget().getClass() + "#"
                        + action.getMethodInvoker());
            }
            response = constructResponse(action.getTarget(), action
                    .getMethodInvoker().getReturnType(), action.invoke());
            if (logger_.isDebugEnabled()) {
                logger_.debug("RESPONSE: " + response);
            }
        }

        return response;
    }

    Response adjustResponse(Dispatch dispatch, Response response, Object page) {
        if (response.getType() == ResponseType.PASSTHROUGH) {
            response = constructDefaultResponse(dispatch, page);
        }

        if (logger_.isDebugEnabled()) {
            logger_.debug("[4]RESPONSE: " + response);
        }

        return response;
    }

    Response constructDefaultResponse(Dispatch dispatch, Object page) {
        if (fileResourceExists(dispatch.getPath())) {
            // パスに対応するテンプレートファイルが存在する場合はパススルーする。
            return PassthroughResponse.INSTANCE;
        } else {
            Object returnValue = dispatch.getMatchedPathMapping()
                    .getDefaultReturnValue();
            if (returnValue != null) {
                return constructResponse(page, returnValue.getClass(),
                        returnValue);
            } else {
                return PassthroughResponse.INSTANCE;
            }
        }
    }

    boolean fileResourceExists(String path) {
        if (path.length() == 0 || path.equals("/")) {
            return false;
        }
        String normalized;
        if (path.endsWith("/")) {
            normalized = path.substring(0, path.length() - 1);
        } else {
            normalized = path;
        }
        Set resourceSet = getServletContext().getResourcePaths(
                normalized.substring(0, normalized.lastIndexOf('/') + 1));
        return (resourceSet != null && resourceSet.contains(normalized));
    }

    @SuppressWarnings("unchecked")
    Response constructResponse(Object page, Class<?> type, Object returnValue) {
        ResponseConstructor<?> constructor = responseConstructorSelector_
                .getResponseConstructor(type);
        if (constructor == null) {
            throw new ComponentNotFoundRuntimeException(
                    "Can't find ResponseConstructor for type '" + type
                            + "' in ResponseConstructorSelector");
        }

        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            if (page != null) {
                // TODO request.getComponentClass().getClassLoader()にすべきか？
                Thread.currentThread().setContextClassLoader(
                        page.getClass().getClassLoader());
            }
            return ((ResponseConstructor<Object>) constructor)
                    .constructResponse(page, returnValue);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    protected class VisitorForPreparing extends PageComponentVisitor {
        private Request request_;

        public VisitorForPreparing(Request request) {
            request_ = request;
        }

        public Object process(PageComponent pageComponent) {
            Object page = pageComponent.getPage();
            PagePropertyMetaData bag = new PagePropertyMetaDataImpl(
                    pageComponent.getPageClass(), getS2Container());
            pageComponent.setRelatedObject(PagePropertyMetaData.class, bag);

            // リクエストパラメータをinjectする。
            pageProcessor_.injectRequestParameters(page, bag, request_
                    .getParameterMap());

            // FormFileのリクエストパラメータをinjectする。
            pageProcessor_.injectRequestFileParameters(page, bag, request_
                    .getFileParameterMap());

            // 各コンテキストが持つ属性をinjectする。
            pageProcessor_.injectContextAttributes(page, request_
                    .getCurrentDispatch().getActionName(), bag);

            return null;
        }
    }

    protected class VisitorForRendering extends PageComponentVisitor {
        public Object process(PageComponent pageComponent) {
            try {
                invokeAction(new ActionImpl(pageComponent.getPage(),
                        new MethodInvokerImpl(MethodUtils.getMethod(
                                pageComponent.getPageClass(), METHOD_RENDER),
                                new Object[0])));

                return null;
            } catch (PermissionDeniedException ex) {
                throw new WrappingRuntimeException(ex);
            }
        }
    }

    protected class VisitorForFinishing extends PageComponentVisitor {
        private Dispatch dispatch_;

        public VisitorForFinishing(Dispatch dispatch) {
            dispatch_ = dispatch;
        }

        public Object process(PageComponent pageComponent) {
            // 各コンテキストに属性をoutjectする。
            pageProcessor_.outjectContextAttributes(pageComponent.getPage(),
                    dispatch_.getActionName(), pageComponent
                            .getRelatedObject(PagePropertyMetaData.class));

            return null;
        }
    }
}