package org.seasar.ymir.impl;

import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.cms.pluggable.ThreadContext;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.Action;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.ExceptionProcessor;
import org.seasar.ymir.Globals;
import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageComponentVisitor;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.Updater;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.cache.CacheManager;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;
import org.seasar.ymir.response.ForwardResponse;
import org.seasar.ymir.util.ClassUtils;
import org.seasar.ymir.util.ResponseUtils;
import org.seasar.ymir.util.ThrowableUtils;
import org.seasar.ymir.util.YmirUtils;

public class ExceptionProcessorImpl implements ExceptionProcessor {
    private static final String METHODNAME_HANDLE = "handle";

    private Ymir ymir_;

    private ActionManager actionManager_;

    private AnnotationHandler annotationHandler_;

    private ApplicationManager applicationManager_;

    private Updater[] updaters_ = new Updater[0];

    private YmirProcessInterceptor[] ymirProcessInterceptors_ = new YmirProcessInterceptor[0];

    private Map<Class<?>, ExceptionHandlerActionMethodHolder> actionMethodHolderMap_;

    private final Log log_ = LogFactory.getLog(ExceptionProcessorImpl.class);

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
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setCacheManager(CacheManager cacheManager) {
        actionMethodHolderMap_ = cacheManager.newMap();
    }

    public void setUpdaters(Updater[] updaters) {
        updaters_ = updaters;
    }

    @Binding(value = "@org.seasar.ymir.util.ContainerUtils@findAllComponents(container, @org.seasar.ymir.interceptor.YmirProcessInterceptor@class)", bindingType = BindingType.MUST)
    public void setYmirProcessInterceptors(
            final YmirProcessInterceptor[] ymirProcessInterceptors) {
        ymirProcessInterceptors_ = ymirProcessInterceptors;
        YmirUtils.sortYmirProcessInterceptors(ymirProcessInterceptors_);
    }

    @SuppressWarnings( { "unchecked", "deprecation" })
    public Response process(Request request, Throwable target,
            boolean useHandlerInPage) {
        if (log_.isDebugEnabled()) {
            log_.debug("Exception has occured", target);
        }

        request.removeAttribute(ATTR_HANDLER);
        request.removeAttribute(ATTR_HANDLER_GLOBAL);
        request.removeAttribute(ATTR_EXCEPTION);

        target = ThrowableUtils.unwrap(target);

        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
            Response response = ymirProcessInterceptors_[i]
                    .exceptionProcessingStarted(request, target);
            if (response != null) {
                if (log_.isDebugEnabled()) {
                    log_.debug("Response has been created by: "
                            + ymirProcessInterceptors_[i] + ": " + response);
                }
                return response;
            }
        }

        if (ymir_.isUnderDevelopment()) {
            for (int i = 0; i < updaters_.length; i++) {
                Response response = updaters_[i].updateByException(request,
                        target);
                if (response != null) {
                    if (log_.isDebugEnabled()) {
                        log_.debug("Response has been created by: "
                                + updaters_[i] + ": " + response);
                    }
                    return response;
                }
            }
        }

        Class<?> exceptionClass = null;
        Object handler = null;
        boolean global = false;
        Response response = null;

        if (useHandlerInPage) {
            PageComponent pageComponent = request.getCurrentDispatch()
                    .getPageComponent();
            if (pageComponent != null) {
                VisitorForProcessingExceptionHandler visitor = new VisitorForProcessingExceptionHandler(
                        request, target, request.getActionName());
                try {
                    exceptionClass = target.getClass();
                    do {
                        visitor.setExceptionClass(exceptionClass);
                        response = pageComponent.accept(visitor);
                        if (response != null) {
                            handler = visitor.getHandler();
                            break;
                        }
                    } while ((exceptionClass = exceptionClass.getSuperclass()) != Object.class);
                } catch (Throwable t) {
                    target = ThrowableUtils.unwrap(t);
                    if (log_.isDebugEnabled()) {
                        log_.debug("In-page exception"
                                + " handler re-throwed exception", target);
                    }
                }
            }
        }

        if (response == null) {
            global = true;

            S2Container container = getS2Container();
            ComponentDef handlerCd = null;
            exceptionClass = target.getClass();
            do {
                String componentName = getComponentName(exceptionClass);
                if (container.hasComponentDef(componentName)) {
                    handlerCd = container.getComponentDef(componentName);
                    break;
                }
            } while ((exceptionClass = exceptionClass.getSuperclass()) != Object.class);

            if (handlerCd == null) {
                // 見つからなかった場合はデフォルトのハンドラを探す。
                // こうしているのは、(ExceptionHandler)Creatorで定義したコンポーネントは
                // あらゆるコンポーネント定義よりも優先順位が低くなってしまうため。
                exceptionClass = target.getClass();
                do {
                    String componentName = NAMEPREFIX_DEFAULT
                            + getComponentName(exceptionClass);
                    if (container.hasComponentDef(componentName)) {
                        handlerCd = container.getComponentDef(componentName);
                        break;
                    }
                } while ((exceptionClass = exceptionClass.getSuperclass()) != Object.class);
            }

            // この時点でhandlerCdがnullならymir-convention.diconの記述ミス。

            handler = handlerCd.getComponent();
            Class<?> handlerClass = handlerCd.getComponentClass();
            Method actionMethod = findActionMethod(handlerClass, true,
                    exceptionClass, null, true);
            if (actionMethod == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Exception handler class must have"
                        + " a method annotated by @ExceptionHandler");
                if (isExceptionHandlerInterfaceEnabled()) {
                    sb.append(", or must implements ExceptionHandler interface"
                            + " with valid concrete parameter type");
                }
                sb.append(": ").append(handlerClass.getName());
                throw new IllegalClientCodeRuntimeException(sb.toString());
            }
            response = process(request, handler, true, handlerClass,
                    actionMethod, exceptionClass, target);
        }

        if (log_.isDebugEnabled()) {
            log_.debug("Raw response (1): " + response);
        }

        if (isExceptionHandlerInterfaceEnabled()) {
            // 互換性のため。
            if (handler instanceof org.seasar.ymir.handler.ExceptionHandler
                    && response.getType() == ResponseType.PASSTHROUGH) {
                response = new ForwardResponse(ResponseUtils
                        .getExceptionTemplatePath(exceptionClass));
            }
        }

        if (log_.isDebugEnabled()) {
            log_.debug("Raw response (2): " + response);
        }

        // ExceptionHandlerコンポーネントと例外オブジェクトをattributeとしてバインドしておく。
        request.setAttribute(ATTR_HANDLER, handler);
        request.setAttribute(ATTR_HANDLER_GLOBAL, global);
        request.setAttribute(ATTR_EXCEPTION, target);

        ThreadContext context = getThreadContext();
        context.setComponent(Response.class, response);

        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
            response = ymirProcessInterceptors_[i]
                    .responseCreatedByExceptionHandler(request, response,
                            handler, global);
            context.setComponent(Response.class, response);
        }

        return response;
    }

    Response process(Request request, Object handler, boolean global,
            Class<?> handlerClass, Method actionMethod,
            Class<?> exceptionClass, Throwable target) {
        if (log_.isDebugEnabled()) {
            log_.debug("Process exception handling. ExceptionHandler: "
                    + handler);
        }

        Action action = newAction(handler, handlerClass, actionMethod, target);
        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
            action = ymirProcessInterceptors_[i]
                    .exceptionHandlerActionInvoking(request, action, global);
        }

        return actionManager_.invokeAction(action);
    }

    @SuppressWarnings("deprecation")
    Method findActionMethod(Class<?> handlerClass, boolean global,
            Class<?> exceptionClass, String actionName, boolean checkInterface) {
        Method method = getActionMethodHolder(handlerClass, global).getMethod(
                new ExceptionHandlerActionMethodCondition(exceptionClass,
                        actionName));
        if (method == null && checkInterface
                && isExceptionHandlerInterfaceEnabled()) {
            if (org.seasar.ymir.handler.ExceptionHandler.class
                    .isAssignableFrom(handlerClass)) {
                Method[] methods = ClassUtils.getMethods(handlerClass,
                        METHODNAME_HANDLE);
                if (methods.length > 0) {
                    method = methods[0];
                }
            }
        }
        return method;
    }

    ExceptionHandlerActionMethodHolder getActionMethodHolder(
            Class<?> handlerClass, boolean global) {
        ExceptionHandlerActionMethodHolder methodHolder = actionMethodHolderMap_
                .get(handlerClass);
        if (methodHolder == null) {
            methodHolder = new ExceptionHandlerActionMethodHolder(handlerClass,
                    global, annotationHandler_);
            actionMethodHolderMap_.put(handlerClass, methodHolder);
        }
        return methodHolder;
    }

    Action newAction(Object handler, Class<?> handlerClass, Method method,
            Throwable targetThrowable) {
        return actionManager_.newAction(handler, handlerClass, method,
                new Object[] { targetThrowable });
    }

    String getComponentName(Class<?> clazz) {
        return Introspector.decapitalize(ClassUtils.getShortName(clazz))
                + SUFFIX_HANDLER;
    }

    S2Container getS2Container() {
        return ymir_.getApplication().getS2Container();
    }

    boolean isExceptionHandlerInterfaceEnabled() {
        return PropertyUtils
                .valueOf(
                        applicationManager_
                                .findContextApplication()
                                .getProperty(
                                        Globals.APPKEY_CORE_HANDLER_EXCEPTIONHANDLERINTERFACE_ENABLE),
                        true);
    }

    ThreadContext getThreadContext() {
        return (ThreadContext) ymir_.getApplication().getS2Container()
                .getRoot().getComponent(ThreadContext.class);
    }

    protected class VisitorForProcessingExceptionHandler extends
            PageComponentVisitor<Response> {
        private Request request_;

        private Class<?> exceptionClass_;

        private Throwable target_;

        private String actionName_;

        private Object handler_;

        private Class<?> handlerClass_;

        protected VisitorForProcessingExceptionHandler(Request request,
                Throwable target, String actionName) {
            request_ = request;
            target_ = target;
            actionName_ = actionName;
        }

        public void setExceptionClass(Class<?> exceptionClass) {
            exceptionClass_ = exceptionClass;
        }

        public Response process(PageComponent pageComponent) {
            handlerClass_ = pageComponent.getPageClass();
            Method actionMethod = findActionMethod(handlerClass_, false,
                    exceptionClass_, actionName_, false);
            if (actionMethod != null) {
                if (log_.isDebugEnabled()) {
                    log_.debug("Exception handler "
                            + ClassUtils.getShorterName(actionMethod
                                    .getDeclaringClass()) + "#"
                            + actionMethod.getName()
                            + "() is handling the exception");
                }
                handler_ = pageComponent.getPage();
                return ExceptionProcessorImpl.this.process(request_, handler_,
                        false, handlerClass_, actionMethod, exceptionClass_,
                        target_);
            } else {
                return null;
            }
        }

        public Object getHandler() {
            return handler_;
        }

        public Class<?> getHandlerClass() {
            return handlerClass_;
        }
    }
}
