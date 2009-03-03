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
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.ExceptionProcessor;
import org.seasar.ymir.Globals;
import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.PageComponent;
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
    public Response process(Request request, Throwable target) {
        log_.debug("Exception has occured", target);

        boolean exceptionHandlerInterfaceEnabled = isExceptionHandlerInterfaceEnabled();

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

        Object handler = null;
        Class<?> exceptionClass = null;
        Response response = null;

        if (request != null) {
            Dispatch dispatch = request.getCurrentDispatch();
            if (dispatch != null) {
                PageComponent pageComponent = dispatch.getPageComponent();
                if (pageComponent != null) {
                    handler = pageComponent.getPage();
                    Class<?> handlerClass = pageComponent.getPageClass();

                    Method actionMethod;
                    exceptionClass = target.getClass();
                    do {
                        actionMethod = findActionMethod(handlerClass,
                                exceptionClass, false);
                    } while (actionMethod == null
                            && (exceptionClass = exceptionClass.getSuperclass()) != Object.class);

                    if (actionMethod != null) {
                        log_.debug("Exception handler in Page ("
                                + handlerClass.getName() + ")is handling it");
                        try {
                            response = process(request, handler, handlerClass,
                                    actionMethod, exceptionClass, target);
                        } catch (Throwable t) {
                            target = ThrowableUtils.unwrap(t);
                            log_
                                    .debug(
                                            "In-page exception handler re-throwed exception",
                                            target);
                        }
                    }
                }
            }
        }

        if (response == null) {
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
            Method actionMethod = findActionMethod(handlerClass,
                    exceptionClass, true);
            if (actionMethod == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Exception handler class must have"
                        + " a method annotated by @ExceptionHandler");
                if (exceptionHandlerInterfaceEnabled) {
                    sb.append(", or must implements ExceptionHandler interface"
                            + " with valid concrete parameter type");
                }
                sb.append(": ").append(handlerClass.getName());
                throw new IllegalClientCodeRuntimeException(sb.toString());
            }
            response = process(request, handler, handlerClass, actionMethod,
                    exceptionClass, target);
        }

        if (log_.isDebugEnabled()) {
            log_.debug("Raw response (1): " + response);
        }

        if (exceptionHandlerInterfaceEnabled) {
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
        request.setAttribute(ATTR_EXCEPTION, target);

        return response;
    }

    Response process(Request request, Object handler, Class<?> handlerClass,
            Method actionMethod, Class<?> exceptionClass, Throwable target) {
        if (log_.isDebugEnabled()) {
            log_.debug("Process exception handling. ExceptionHandler: "
                    + handler);
        }

        final Action originalAction = getAction(handler, handlerClass,
                actionMethod, target);
        Action action = originalAction;
        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
            action = ymirProcessInterceptors_[i]
                    .exceptionHandlerActionInvoking(request, originalAction,
                            action);
        }

        return actionManager_.invokeAction(action);
    }

    @SuppressWarnings("deprecation")
    Method findActionMethod(Class<?> handlerClass, Class<?> exceptionClass,
            boolean checkInterface) {
        Method method = getActionMethodHolder(handlerClass).getMethod(
                exceptionClass);
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
            Class<?> handlerClass) {
        ExceptionHandlerActionMethodHolder methodHolder = actionMethodHolderMap_
                .get(handlerClass);
        if (methodHolder == null) {
            methodHolder = new ExceptionHandlerActionMethodHolder(handlerClass,
                    annotationHandler_);
            actionMethodHolderMap_.put(handlerClass, methodHolder);
        }
        return methodHolder;
    }

    @SuppressWarnings("deprecation")
    Action getAction(Object handler, Class<?> handlerClass, Method method,
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
}
