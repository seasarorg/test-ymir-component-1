package org.seasar.ymir.impl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.ymir.Action;
import org.seasar.ymir.AttributeContainer;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageComponentVisitor;
import org.seasar.ymir.PageNotFoundException;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.ScopeAttribute;
import org.seasar.ymir.Updater;
import org.seasar.ymir.WrappingRuntimeException;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.annotation.Include;
import org.seasar.ymir.beanutils.FormFileArrayConverter;
import org.seasar.ymir.beanutils.FormFileConverter;
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

    private static final String INDEX_PREFIX = "[";

    private static final String INDEX_SUFFIX = "]";

    private Ymir ymir_;

    private ResponseConstructorSelector responseConstructorSelector_;

    private Updater[] updaters_ = new Updater[0];

    private final PropertyUtilsBean propertyUtilsBean_ = new PropertyUtilsBean();

    private final BeanUtilsBean beanUtilsBean_;

    private YmirProcessInterceptor[] ymirProcessInterceptors_ = new YmirProcessInterceptor[0];

    private final Logger logger_ = Logger.getLogger(getClass());

    private PageComponentVisitor visitorForRendering_ = new VisitorForRendering();

    public DefaultRequestProcessor() {
        ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
        convertUtilsBean.register(new FormFileConverter(), FormFile.class);
        convertUtilsBean.register(new FormFileArrayConverter(),
                FormFile[].class);
        beanUtilsBean_ = new BeanUtilsBean(convertUtilsBean, propertyUtilsBean_);
        DisposableUtil.add(new Disposable() {
            public void dispose() {
                propertyUtilsBean_.clearDescriptors();
            }
        });
    }

    public void setYmir(Ymir ymir) {
        ymir_ = ymir;
    }

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
        if (Request.DISPATCHER_REQUEST.equals(request.getCurrentDispatch()
                .getDispatcher())) {
            if (request.isDenied()) {
                throw new PageNotFoundException(request.getPath());
            }

            Response response = PassthroughResponse.INSTANCE;
            if (request.isMatched()) {
                PageComponent pageComponent = createPageComponent(request
                        .getPageComponentName());
                if (pageComponent != null) {
                    request.setPageComponent(pageComponent);

                    for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
                        pageComponent = ymirProcessInterceptors_[i]
                                .pageComponentCreated(pageComponent);
                    }

                    // リクエストに対応するアクションを決定する。
                    Action action = request.getMatchedPathMapping().getAction(
                            pageComponent, request);
                    if (action == null) {
                        // リクエストに対応するアクションが存在しない場合はリクエストを受け付けない。
                        throw new PermissionDeniedException("Action not found");
                    }
                    request.setAction(action);

                    pageComponent.accept(new VisitorForPreparing(request));

                    Action actualAction = action;
                    for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
                        actualAction = ymirProcessInterceptors_[i]
                                .actionInvoking(action, request, actualAction);
                    }

                    response = normalizeResponse(adjustResponse(request,
                            invokeAction(actualAction), pageComponent), request
                            .getPath());

                    ResponseType responseType = response.getType();
                    if (responseType == ResponseType.PASSTHROUGH
                            || responseType == ResponseType.FORWARD) {
                        // 画面描画のためのAction呼び出しを行なう。
                        pageComponent.accept(visitorForRendering_);
                    }

                    pageComponent.accept(new VisitorForFinishing(request));

                    // Pageコンポーネントをattributeとしてバインドしておく。
                    request.setAttribute(ATTR_PAGECOMPONENT, pageComponent);
                    request.setAttribute(ATTR_SELF, pageComponent.getPage());
                } else if (request.getPageComponentName() != null) {
                    // pageComponentがnullになるのは、page名が割り当てられているのにまだ
                    // 対応するPageクラスが作成されていない場合、またはDeniedPathMappingImpl
                    // に関して処理をしている場合。
                    // 前者の場合自動生成機能でクラスやテンプレートの自動生成が適切にできるように、
                    // デフォルト値からResponseを作るようにしている。
                    // （例えば、リクエストパス名がテンプレートパス名ではない場合に、リクエストパス名で
                    // テンプレートが作られてしまうとうれしくない。）
                    response = normalizeResponse(constructDefaultResponse(
                            request, null), request.getPath());
                }
            }

            if (logger_.isDebugEnabled()) {
                logger_.debug("FINAL RESPONSE: " + response);
            }

            if (ymir_.isUnderDevelopment()) {
                for (int i = 0; i < updaters_.length; i++) {
                    Response newResponse = updaters_[i].update(request,
                            response);
                    if (newResponse != response) {
                        return newResponse;
                    }
                }
            }

            return response;
        } else {
            // includeの場合はselfを設定するだけ。
            Object page = getPage(request.getCurrentDispatch()
                    .getPageComponentName());
            if (page != null) {
                request.setAttribute(ATTR_SELF, page);
            }

            return PassthroughResponse.INSTANCE;
        }
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

    void injectRequestParameters(Object page, PagePropertyBag bag,
            Map properties) {
        for (Iterator itr = properties.keySet().iterator(); itr.hasNext();) {
            String name = (String) itr.next();
            if (name == null || bag.isProtected(name)) {
                continue;
            }
            try {
                beanUtilsBean_.setProperty(page, name, properties.get(name));
            } catch (Throwable t) {
                if (logger_.isDebugEnabled()) {
                    logger_.debug("Can't populate property '" + name + "'", t);
                }
            }
        }
    }

    void injectRequestFileParameters(Object page, PagePropertyBag bag,
            Map properties) {
        for (Iterator itr = properties.keySet().iterator(); itr.hasNext();) {
            String name = (String) itr.next();
            if (name == null || bag.isProtected(name)) {
                continue;
            }
            if (beanUtilsBean_.getPropertyUtils().isWriteable(page, name)) {
                try {
                    beanUtilsBean_.copyProperty(page, name, properties
                            .get(name));
                } catch (Throwable t) {
                    if (logger_.isDebugEnabled()) {
                        logger_.debug("Can't copy property '" + name + "'", t);
                    }
                }
            }
        }
    }

    void injectContextAttributes(Object page, String actionName,
            PagePropertyBag bag) {
        ScopeAttribute[] attributes = bag.getInjectedScopeAttributes();
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].isEnable(actionName)) {
                attributes[i].injectTo(page);
            }
        }
    }

    void outjectContextAttributes(Object page, String actionName,
            PagePropertyBag bag) {
        ScopeAttribute[] attributes = bag.getOutjectedScopeAttributes();
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].isEnable(actionName)) {
                attributes[i].outjectFrom(page);
            }
        }
    }

    Response invokeAction(final Action action) throws PermissionDeniedException {
        Response response = PassthroughResponse.INSTANCE;

        if (action.shouldInvoke()) {
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

    Response adjustResponse(Request request, Response response, Object page) {
        if (response.getType() == ResponseType.PASSTHROUGH) {
            response = constructDefaultResponse(request, page);
        }

        if (logger_.isDebugEnabled()) {
            logger_.debug("[4]RESPONSE: " + response);
        }

        return response;
    }

    Response constructDefaultResponse(Request request, Object page) {
        if (fileResourceExists(request.getPath())) {
            // パスに対応するテンプレートファイルが存在する場合はパススルーする。
            return PassthroughResponse.INSTANCE;
        } else {
            Object returnValue = request.getMatchedPathMapping()
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
            PagePropertyBag bag = new PagePropertyBag(pageComponent
                    .getPageClass(), getS2Container());
            pageComponent.setRelatedObject(PagePropertyBag.class, bag);

            // リクエストパラメータをinjectする。
            injectRequestParameters(page, bag, request_.getParameterMap());

            // FormFileのリクエストパラメータをinjectする。
            injectRequestFileParameters(page, bag, request_
                    .getFileParameterMap());

            // 各コンテキストが持つ属性をinjectする。
            injectContextAttributes(page, request_.getAction().getName(), bag);

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
        private Request request_;

        public VisitorForFinishing(Request request) {
            request_ = request;
        }

        public Object process(PageComponent pageComponent) {
            // 各コンテキストに属性をoutjectする。
            outjectContextAttributes(pageComponent.getPage(), request_
                    .getAction().getName(), pageComponent
                    .getRelatedObject(PagePropertyBag.class));

            return null;
        }
    }
}