package org.seasar.ymir.impl;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.seasar.cms.pluggable.ThreadContext;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.kvasir.util.el.VariableResolver;
import org.seasar.ymir.AttributeContainer;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.PageNotFoundException;
import org.seasar.ymir.PathMapping;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.ScopeAttribute;
import org.seasar.ymir.Updater;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.beanutils.FormFileArrayConverter;
import org.seasar.ymir.beanutils.FormFileConverter;
import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;
import org.seasar.ymir.response.PassthroughResponse;
import org.seasar.ymir.response.constructor.ResponseConstructor;
import org.seasar.ymir.response.constructor.ResponseConstructorSelector;
import org.seasar.ymir.util.MethodUtils;

public class DefaultRequestProcessor implements RequestProcessor {

    public static final String PARAM_METHOD = "__ymir__method";

    static final Set<ConstraintType> EMPTY_SUPPRESSTYPESET = EnumSet
            .noneOf(ConstraintType.class);

    private static final String INDEX_PREFIX = "[";

    private static final String INDEX_SUFFIX = "]";

    private Ymir ymir_;

    private ResponseConstructorSelector responseConstructorSelector_;

    private Updater[] updaters_ = new Updater[0];

    private final PropertyUtilsBean propertyUtilsBean_ = new PropertyUtilsBean();

    private final BeanUtilsBean beanUtilsBean_;

    private ThreadContext threadContext_;

    private YmirProcessInterceptor[] ymirProcessInterceptors_ = new YmirProcessInterceptor[0];

    private final Logger logger_ = Logger.getLogger(getClass());

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
    }

    public Request prepareForProcessing(String contextPath, String path,
            String method, String dispatcher,
            Map<String, String[]> parameterMap,
            Map<String, FormFile[]> fileParameterMap,
            AttributeContainer attributeContainer, Locale locale) {

        if (ymir_.isUnderDevelopment()) {
            method = correctMethod(method, parameterMap);
        }

        MatchedPathMapping matched = findMatchedPathMapping(path, method);

        Request request = new RequestImpl(contextPath, path, method,
                dispatcher, parameterMap, fileParameterMap, attributeContainer,
                locale, matched);
        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
            request = ymirProcessInterceptors_[i].requestCreated(request);
        }
        return request;
    }

    String correctMethod(String method, Map parameterMap) {
        String[] values = (String[]) parameterMap.get(PARAM_METHOD);
        if (values != null && values.length > 0) {
            return values[0];
        } else {
            return method;
        }
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

    protected PathMapping[] getPathMappings() {
        return ymir_.getApplication().getPathMappingProvider()
                .getPathMappings();
    }

    ServletContext getServletContext() {
        return (ServletContext) getRootS2Container().getComponent(
                ServletContext.class);
    }

    protected S2Container getS2Container() {
        return ymir_.getApplication().getS2Container();
    }

    protected S2Container getRootS2Container() {
        return SingletonS2ContainerFactory.getContainer();
    }

    public MatchedPathMapping findMatchedPathMapping(String path, String method) {

        VariableResolver resolver = null;
        PathMapping[] pathMappings = getPathMappings();
        if (pathMappings != null) {
            for (int i = 0; i < pathMappings.length; i++) {
                resolver = pathMappings[i].match(path, method);
                if (resolver != null) {
                    return new MatchedPathMappingImpl(pathMappings[i], resolver);
                }
            }
        }
        return null;
    }

    public Response process(Request request) throws PageNotFoundException,
            PermissionDeniedException {

        if (request.isDenied()) {
            throw new PageNotFoundException(request.getPath());
        }

        Response response = null;
        if (request.isMatched()) {
            Object component = getComponent(request);
            if (component != null) {
                Class<?> componentClass = getComponentClass(request
                        .getComponentName());
                request.setComponentClass(componentClass);

                for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
                    component = ymirProcessInterceptors_[i]
                            .componentCreated(component);
                }

                PagePropertyBag bag = new PagePropertyBag(componentClass,
                        getS2Container());

                prepareForComponent(component, bag, request);

                // リクエストに対応するアクションの呼び出しを行なう。
                MethodInvoker action = request.getMatchedPathMapping()
                        .getActionMethodInvoker(componentClass, request);
                if (action.getMethod() != null) {
                    request.setActionName(action.getMethod().getName());
                }

                response = normalizeResponse(adjustResponse(request,
                        invokeMethod(component, action, request, true),
                        component), request.getPath());

                ResponseType responseType = response.getType();
                if (responseType == ResponseType.PASSTHROUGH
                        || responseType == ResponseType.FORWARD) {
                    // 画面描画のためのAction呼び出しを行なう。
                    // （画面描画のためのAction呼び出しの際にはAction付随の制約以外の
                    // 制約チェックを行なわない。）
                    invokeMethod(component, new MethodInvokerImpl(MethodUtils
                            .getMethod(componentClass, METHOD_RENDER),
                            new Object[0]), request, false);
                }

                finishForComponent(component, bag, request);
            } else {
                // componentがnullになるのは、component名が割り当てられているのにまだ
                // 対応するcomponentクラスが作成されていない場合。
                // その場合自動生成機能でクラスやテンプレートの自動生成が適切にできるように、
                // デフォルト値からResponseを作るようにしている。
                // （例えば、リクエストパス名がテンプレートパス名ではない場合に、リクエストパス名で
                // テンプレートが作られてしまうとうれしくない。）

                response = normalizeResponse(constructDefaultResponse(request,
                        component), request.getPath());
            }
        } else {
            response = PassthroughResponse.INSTANCE;
        }

        if (logger_.isDebugEnabled()) {
            logger_.debug("FINAL RESPONSE: " + response);
        }

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

    protected Object getComponent(Request request) {
        Object component = null;
        S2Container s2container = getS2Container();
        if (s2container.hasComponentDef(request.getComponentName())) {
            ThreadContext context = getThreadContext();
            try {
                context.setComponent(Request.class, request);
                component = s2container
                        .getComponent(request.getComponentName());
            } finally {
                context.setComponent(Request.class, null);
            }
        }
        return component;
    }

    protected Class<?> getComponentClass(String componentName) {
        ComponentDef componentDef = getS2Container().getComponentDef(
                componentName);
        if (componentDef != null) {
            return componentDef.getComponentClass();
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

    void prepareForComponent(Object component, PagePropertyBag bag,
            Request request) {
        // リクエストパラメータをinjectする。
        injectRequestParameters(component, bag, request.getParameterMap());

        // FormFileのリクエストパラメータをinjectする。
        injectRequestFileParameters(component, bag, request
                .getFileParameterMap());

        // 各コンテキストが持つ属性をinjectする。
        injectContextAttributes(component, bag);
    }

    void injectRequestParameters(Object component, PagePropertyBag bag,
            Map properties) {
        for (Iterator itr = properties.keySet().iterator(); itr.hasNext();) {
            String name = (String) itr.next();
            if (name == null || bag.isProtected(name)) {
                continue;
            }
            try {
                beanUtilsBean_.setProperty(component, name, properties
                        .get(name));
            } catch (Throwable t) {
                if (logger_.isDebugEnabled()) {
                    logger_.debug("Can't populate property '" + name + "'", t);
                }
            }
        }
    }

    void injectRequestFileParameters(Object component, PagePropertyBag bag,
            Map properties) {
        for (Iterator itr = properties.keySet().iterator(); itr.hasNext();) {
            String name = (String) itr.next();
            if (name == null || bag.isProtected(name)) {
                continue;
            }
            if (beanUtilsBean_.getPropertyUtils().isWriteable(component, name)) {
                try {
                    beanUtilsBean_.copyProperty(component, name, properties
                            .get(name));
                } catch (Throwable t) {
                    if (logger_.isDebugEnabled()) {
                        logger_.debug("Can't copy property '" + name + "'", t);
                    }
                }
            }
        }
    }

    void injectContextAttributes(Object component, PagePropertyBag bag) {
        ScopeAttribute[] attributes = bag.getInjectedScopeAttributes();
        for (int i = 0; i < attributes.length; i++) {
            attributes[i].injectTo(component);
        }
    }

    void finishForComponent(Object component, PagePropertyBag bag,
            Request request) {
        // 各コンテキストに属性をoutjectする。
        outjectContextAttributes(component, bag);

        // コンポーネント自体をattributeとしてバインドしておく。
        request.setAttribute(ATTR_SELF, component);
    }

    void outjectContextAttributes(Object component, PagePropertyBag bag) {
        ScopeAttribute[] attributes = bag.getOutjectedScopeAttributes();
        for (int i = 0; i < attributes.length; i++) {
            attributes[i].outjectFrom(component);
        }
    }

    ThreadContext getThreadContext() {
        if (threadContext_ == null) {
            threadContext_ = (ThreadContext) getRootS2Container().getComponent(
                    ThreadContext.class);
        }
        return threadContext_;
    }

    Response invokeMethod(Object component, final MethodInvoker methodInvoker,
            Request request, boolean invokeAsAction)
            throws PermissionDeniedException {

        Response response = PassthroughResponse.INSTANCE;

        MethodInvoker actualMethodInvoker = methodInvoker;
        if (invokeAsAction) {
            for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
                actualMethodInvoker = ymirProcessInterceptors_[i]
                        .actionInvoking(component, methodInvoker, request,
                                actualMethodInvoker);
            }
        }

        if (actualMethodInvoker.shouldInvoke()) {
            if (logger_.isDebugEnabled()) {
                logger_.debug("INVOKE: "
                        + request.getComponentClass().getName() + "#"
                        + actualMethodInvoker);
            }
            response = constructResponse(component, methodInvoker
                    .getReturnType(), actualMethodInvoker.invoke(component));
            if (logger_.isDebugEnabled()) {
                logger_.debug("RESPONSE: " + response);
            }
        }

        return response;
    }

    Response adjustResponse(Request request, Response response, Object component) {
        if (response.getType() == ResponseType.PASSTHROUGH) {
            response = constructDefaultResponse(request, component);
        }

        if (logger_.isDebugEnabled()) {
            logger_.debug("[4]RESPONSE: " + response);
        }

        return response;
    }

    Response constructDefaultResponse(Request request, Object component) {
        if (fileResourceExists(request.getPath())) {
            // パスに対応するテンプレートファイルが存在する場合はパススルーする。
            return PassthroughResponse.INSTANCE;
        } else {
            Object returnValue = request.getMatchedPathMapping()
                    .getDefaultReturnValue();
            if (returnValue != null) {
                return constructResponse(component, returnValue.getClass(),
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
    Response constructResponse(Object component, Class<?> type,
            Object returnValue) {

        ResponseConstructor<?> constructor = responseConstructorSelector_
                .getResponseConstructor(type);
        if (constructor == null) {
            throw new ComponentNotFoundRuntimeException(
                    "Can't find ResponseConstructor for type '" + type
                            + "' in ResponseConstructorSelector");
        }

        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            if (component != null) {
                // TODO request.getComponentClass().getClassLoader()にすべきか？
                Thread.currentThread().setContextClassLoader(
                        component.getClass().getClassLoader());
            }
            return ((ResponseConstructor<Object>) constructor)
                    .constructResponse(component, returnValue);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }
}
