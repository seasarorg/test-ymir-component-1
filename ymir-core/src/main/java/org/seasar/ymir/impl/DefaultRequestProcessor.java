package org.seasar.ymir.impl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.seasar.cms.pluggable.ThreadContext;
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
import org.seasar.ymir.Notes;
import org.seasar.ymir.PageNotFoundException;
import org.seasar.ymir.PathMapping;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.ScopeAttribute;
import org.seasar.ymir.Updater;
import org.seasar.ymir.WrappingRuntimeException;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.annotation.SuppressConstraints;
import org.seasar.ymir.annotation.Validator;
import org.seasar.ymir.beanutils.FormFileArrayConverter;
import org.seasar.ymir.beanutils.FormFileConverter;
import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.ConstraintAnnotation;
import org.seasar.ymir.response.PassthroughResponse;
import org.seasar.ymir.response.constructor.ResponseConstructor;
import org.seasar.ymir.response.constructor.ResponseConstructorSelector;

public class DefaultRequestProcessor implements RequestProcessor {

    public static final String PARAM_METHOD = "__ymir__method";

    static final Set<ConstraintType> EMPTY_SUPPRESSTYPESET = EnumSet
            .noneOf(ConstraintType.class);

    private Ymir ymir_;

    private ResponseConstructorSelector responseConstructorSelector_;

    private Updater[] updaters_ = new Updater[0];

    private final PropertyUtilsBean propertyUtilsBean_ = new PropertyUtilsBean();

    private final BeanUtilsBean beanUtilsBean_;

    private ThreadContext threadContext_;

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

    public Request prepareForProcessing(String contextPath, String path,
            String method, String dispatcher,
            Map<String, String[]> parameterMap,
            Map<String, FormFile[]> fileParameterMap,
            AttributeContainer attributeContainer, Locale locale) {

        if (ymir_.isUnderDevelopment()) {
            method = correctMethod(method, parameterMap);
        }

        MatchedPathMapping matched = findMatchedPathMapping(path, method);

        return new RequestImpl(contextPath, path, method, dispatcher,
                parameterMap, fileParameterMap, attributeContainer, locale,
                matched);
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
                    return new MatchedPathMapping(pathMappings[i], resolver);
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

        Response response = PassthroughResponse.INSTANCE;
        if (request.isMatched()) {
            Object component = getComponent(request);
            if (component != null) {
                PagePropertyBag bag = new PagePropertyBag(component.getClass(),
                        getS2Container());

                prepareForComponent(component, bag, request);

                // リクエストに対応するアクションの呼び出しを行なう。
                Method action = getActionMethod(component, request
                        .getActionName(), ACTION_DEFAULT, request
                        .isDispatchingByParameter(), request);
                if (action != null) {
                    request.setActionName(action.getName());
                }
                response = normalizeResponse(adjustResponse(request,
                        invokeAction(component, action, request, true),
                        component), request.getPath());

                ResponseType responseType = response.getType();
                if (responseType == ResponseType.PASSTHROUGH
                        || responseType == ResponseType.FORWARD) {
                    // 画面描画のためのAction呼び出しを行なう。
                    // （画面描画のためのAction呼び出しの際にはAction付随の制約以外の
                    // 制約チェックを行なわない。）
                    invokeAction(component, getActionMethod(component,
                            ACTION_RENDER, null, false, request), request,
                            false);
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

    Response normalizeResponse(Response response, String path) {
        if (response.getType() == ResponseType.FORWARD
                && response.getPath().equals(path)) {
            return PassthroughResponse.INSTANCE;
        } else {
            return response;
        }
    }

    Notes confirmConstraint(Object component, Method action, Request request)
            throws PermissionDeniedException {

        boolean validationFailed = false;
        Notes notes = new Notes();
        ConstraintBag[] bag = getConstraintBags(component, action);
        for (int i = 0; i < bag.length; i++) {
            try {
                bag[i].confirm(component, request);
            } catch (PermissionDeniedException ex) {
                throw ex;
            } catch (ValidationFailedException ex) {
                validationFailed = true;
                notes.add(ex.getNotes());
            } catch (ConstraintViolatedException ex) {
                throw new RuntimeException("May logic error", ex);
            }
        }

        // Validatorアノテーションがついているメソッドを実行する。
        Method[] validators = gatherValidators(component);
        for (int i = 0; i < validators.length; i++) {
            try {
                Object invoked = validators[i].invoke(component, new Object[0]);
                if (invoked instanceof Notes) {
                    Notes ns = (Notes) invoked;
                    if (!ns.isEmpty()) {
                        validationFailed = true;
                        notes.add(ns);
                    }
                }
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException("May logic error", ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException("May logic error", ex);
            } catch (InvocationTargetException ex) {
                Throwable cause = ex.getCause();
                if (cause instanceof ValidationFailedException) {
                    validationFailed = true;
                    notes.add(((ValidationFailedException) cause).getNotes());
                }
            }
        }

        if (validationFailed) {
            return notes;
        } else {
            return null;
        }
    }

    Method[] gatherValidators(Object component) {
        List<Method> validatorList = new ArrayList<Method>();
        Method[] methods = component.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].isAnnotationPresent(Validator.class)) {
                if (methods[i].getParameterTypes().length > 0) {
                    // 引数を持つメソッドにはValidatorアノテーションはつけられない。
                    throw new RuntimeException(
                            "@Validator must be annotated on a method that has no parameters");
                }
                validatorList.add(methods[i]);
            }
        }
        return validatorList.toArray(new Method[0]);
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

    Response invokeAction(Object component, Method action, Request request,
            boolean confirmConstraints) throws PermissionDeniedException {

        Response response = PassthroughResponse.INSTANCE;
        Object[] params = new Object[0];

        if (confirmConstraints) {
            // 制約チェックを行なう。
            try {
                Notes notes = confirmConstraint(component, action, request);
                if (notes != null) {
                    request.setAttribute(ATTR_NOTES, notes);

                    // バリデーションエラーが発生した場合は、エラー処理メソッドが存在すればそれを呼び出す。
                    // メソッドが存在しなければ何もしない（元のアクションメソッドの呼び出しをスキップする）。
                    action = getActionMethod(component,
                            ACTION_VALIDATIONFAILED,
                            new Class[] { Notes.class });
                    if (action != null) {
                        params = new Object[] { notes };
                    } else {
                        action = getActionMethod(component,
                                ACTION_VALIDATIONFAILED);
                    }
                }
            } catch (PermissionDeniedException ex) {
                // 権限エラーが発生した場合は、エラー処理メソッドが存在すればそれを呼び出す。
                // メソッドが存在しなければPermissionDeniedExceptionを上に再スローする。
                action = getActionMethod(component, ACTION_PERMISSIONDENIED,
                        new Class[] { PermissionDeniedException.class });
                if (action != null) {
                    params = new Object[] { ex };
                } else {
                    action = getActionMethod(component, ACTION_PERMISSIONDENIED);
                }
                if (action == null) {
                    throw ex;
                }
            }
        }

        if (action != null) {
            if (logger_.isDebugEnabled()) {
                logger_.debug("INVOKE: " + component.getClass().getName() + "#"
                        + action);
            }
            Object returnValue;
            try {
                returnValue = action.invoke(component, params);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new WrappingRuntimeException(ex.getCause() != null ? ex
                        .getCause() : ex);
            }
            response = constructResponse(component, action.getReturnType(),
                    returnValue);
            if (logger_.isDebugEnabled()) {
                logger_.debug("RESPONSE: " + response);
            }
        }

        return response;
    }

    Method getActionMethod(Object component, String actionName,
            String defaultActionName, boolean dispatchingByParamter,
            Request request) {
        if (dispatchingByParamter) {
            Method[] methods = component.getClass().getMethods();
            if (logger_.isDebugEnabled()) {
                logger_
                        .debug("getActionMethod: dispatchingByRequestParameter=true. search "
                                + component.getClass()
                                + " for "
                                + actionName
                                + " method...");
            }
            List<Method> list = new ArrayList<Method>();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getParameterTypes().length > 0) {
                    continue;
                }
                String name = methods[i].getName();
                if (name.startsWith(actionName)) {
                    String parameterName = request.extractParameterName(name
                            .substring(actionName.length()));
                    if (parameterName != null
                            && request.getParameter(parameterName) != null) {
                        if (logger_.isDebugEnabled()) {
                            logger_.debug("getActionMethod: Found: "
                                    + methods[i]);
                        }
                        return methods[i];
                    }
                }
                list.add(methods[i]);
            }
            if (defaultActionName != null) {
                if (logger_.isDebugEnabled()) {
                    logger_.debug("getActionMethod: search "
                            + component.getClass() + " for "
                            + defaultActionName + " method...");
                }
                for (Iterator itr = list.iterator(); itr.hasNext();) {
                    Method method = (Method) itr.next();
                    String name = method.getName();
                    if (name.startsWith(defaultActionName)) {
                        String parameterName = request
                                .extractParameterName(name
                                        .substring(defaultActionName.length()));
                        if (parameterName != null
                                && request.getParameter(parameterName) != null) {
                            if (logger_.isDebugEnabled()) {
                                logger_.debug("getActionMethod: Found: "
                                        + method);
                            }
                            return method;
                        }
                    }
                }
            }
        }

        Method method = getActionMethod(component, actionName);
        if (method == null && defaultActionName != null) {
            method = getActionMethod(component, defaultActionName);
        }

        if (logger_.isDebugEnabled()) {
            logger_.debug("getActionMethod: Found: " + method);
        }
        return method;
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
            Object returnValue = request.getDefaultReturnValue();
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

    public Method getActionMethod(Object component, String actionName) {

        return getActionMethod(component, actionName, new Class[0]);
    }

    public Method getActionMethod(Object component, String actionName,
            Class[] paramTypes) {

        try {
            return component.getClass().getMethod(actionName, paramTypes);
        } catch (SecurityException ex) {
            return null;
        } catch (NoSuchMethodException ex) {
            return null;
        }
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
                Thread.currentThread().setContextClassLoader(
                        component.getClass().getClassLoader());
            }
            return ((ResponseConstructor<Object>) constructor)
                    .constructResponse(component, returnValue);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    protected ConstraintBag[] getConstraintBags(Object component, Method action) {
        return getConstraints(component.getClass(), action);
    }

    // PropertyDescriptorのreadMethodは対象外。fieldも対象外。
    ConstraintBag[] getConstraints(Class<?> clazz, Method action) {
        List<ConstraintBag> list = new ArrayList<ConstraintBag>();

        Set<ConstraintType> suppressTypeSet = EnumSet
                .noneOf(ConstraintType.class);
        if (action != null) {
            SuppressConstraints suppress = action
                    .getAnnotation(SuppressConstraints.class);
            if (suppress != null) {
                ConstraintType[] types = suppress.value();
                for (int i = 0; i < types.length; i++) {
                    suppressTypeSet.add(types[i]);
                }
            }
        }
        getConstraint(clazz, list, suppressTypeSet);
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException ex) {
            throw new RuntimeException(ex);
        }
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < pds.length; i++) {
            getConstraint(pds[i].getWriteMethod(), list, suppressTypeSet);
        }
        getConstraint(action, list, EMPTY_SUPPRESSTYPESET);

        return list.toArray(new ConstraintBag[0]);
    }

    @SuppressWarnings("unchecked")
    void getConstraint(AnnotatedElement element, List<ConstraintBag> list,
            Set<ConstraintType> suppressTypeSet) {
        if (element == null) {
            return;
        }
        Annotation[] annotations = element.getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            ConstraintAnnotation constraintAnnotation = annotations[i]
                    .annotationType().getAnnotation(ConstraintAnnotation.class);
            if (constraintAnnotation == null
                    || suppressTypeSet.contains(constraintAnnotation.type())) {
                continue;
            }
            list.add(new ConstraintBag(((Constraint) getS2Container()
                    .getComponent(constraintAnnotation.component())),
                    annotations[i], element));
        }
    }

    Method getMethod(Class clazz, String name) {
        try {
            return clazz.getMethod(name, new Class[0]);
        } catch (SecurityException ex) {
            return null;
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }
}
