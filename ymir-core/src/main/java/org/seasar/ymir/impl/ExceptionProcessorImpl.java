package org.seasar.ymir.impl;

import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Action;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.ExceptionProcessor;
import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.Updater;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.cache.CacheManager;
import org.seasar.ymir.handler.annotation.ExceptionHandler;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;
import org.seasar.ymir.response.ForwardResponse;
import org.seasar.ymir.response.PassthroughResponse;
import org.seasar.ymir.util.ClassUtils;
import org.seasar.ymir.util.ThrowableUtils;
import org.seasar.ymir.util.YmirUtils;

public class ExceptionProcessorImpl implements ExceptionProcessor {
    private static final String METHODNAME_HANDLE = "handle";

    private Ymir ymir_;

    private ActionManager actionManager_;

    private AnnotationHandler annotationHandler_;

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

    @SuppressWarnings("unchecked")
    public Response process(Request request, Throwable t) {
        t = ThrowableUtils.unwrap(t);
        log_.error("Exception has occured", t);

        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
            Response response = ymirProcessInterceptors_[i]
                    .exceptionProcessingStarted(request, t);
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
                Response response = updaters_[i].updateByException(request, t);
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
        Class<?> handlerClass = null;
        Method actionMethod = null;
        Class<?> exceptionClass = null;
        boolean localHandler = false;
        if (request != null) {
            Dispatch dispatch = request.getCurrentDispatch();
            if (dispatch != null) {
                PageComponent pageComponent = dispatch.getPageComponent();
                if (pageComponent != null) {
                    Object page = pageComponent.getPage();
                    Class<?> pageClass = pageComponent.getPageClass();

                    exceptionClass = t.getClass();
                    do {
                        actionMethod = findActionMethod(pageClass,
                                exceptionClass, false);
                    } while (actionMethod == null
                            && (exceptionClass = exceptionClass.getSuperclass()) != Object.class);
                    if (actionMethod != null) {
                        handler = page;
                        handlerClass = pageClass;
                        localHandler = true;
                    }
                }
            }
        }
        if (actionMethod == null) {
            S2Container container = getS2Container();
            ComponentDef handlerCd = null;
            exceptionClass = t.getClass();
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
                exceptionClass = t.getClass();
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
            handlerClass = handlerCd.getComponentClass();
            actionMethod = findActionMethod(handlerClass, exceptionClass, true);
            if (actionMethod == null) {
                throw new IllegalClientCodeRuntimeException(
                        "Exception handler class must have a method annotated by @ExceptionHandler, "
                                + "or must implements ExceptionHandler interface with valid concrete parameter type: "
                                + handlerClass.getName());
            }
        }
        if (log_.isDebugEnabled()) {
            log_.debug("ExceptionHandler: " + handler);
        }

        final Action originalAction = getAction(handler, handlerClass,
                actionMethod, t);
        Action action = originalAction;
        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
            action = ymirProcessInterceptors_[i]
                    .exceptionHandlerActionInvoking(request, originalAction,
                            action);
        }

        Response response = invokeAction(action);
        if (log_.isDebugEnabled()) {
            log_.debug("Raw response: " + response);
        }

        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
            response = ymirProcessInterceptors_[i]
                    .responseCreatedByExceptionHandler(handler, response);
        }

        if (!localHandler && response.getType() == ResponseType.PASSTHROUGH) {
            response = new ForwardResponse(PATH_EXCEPTION_TEMPLATE
                    + getClassShortName(exceptionClass)
                    + SUFFIX_EXCEPTION_TEMPLATE);
        }

        if (log_.isDebugEnabled()) {
            log_.debug("Final response: " + response);
        }

        // ExceptionHandlerコンポーネントと例外オブジェクトをattributeとしてバインドしておく。
        request.setAttribute(ATTR_HANDLER, handler);
        request.setAttribute(ATTR_EXCEPTION, t);

        return response;
    }

    @SuppressWarnings("deprecation")
    Method findActionMethod(Class<?> handlerClass, Class<?> exceptionClass,
            boolean checkInterface) {
        Method method = getActionMethodHolder(handlerClass).getMethod(
                exceptionClass);
        if (method == null && checkInterface) {
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

    Response invokeAction(Action action) {
        Response response = new PassthroughResponse();

        if (action != null && action.shouldInvoke()) {
            if (log_.isDebugEnabled()) {
                log_.debug("INVOKE: " + action.getTarget().getClass() + "#"
                        + action.getMethodInvoker());
            }
            response = actionManager_.constructResponse(action.getTarget(),
                    action.getReturnType(), action.invoke());
            if (log_.isDebugEnabled()) {
                log_.debug("RESPONSE: " + response);
            }
        }

        return response;
    }

    String getComponentName(Class<?> clazz) {
        return Introspector.decapitalize(getClassShortName(clazz))
                + SUFFIX_HANDLER;
    }

    String getClassShortName(Class<?> clazz) {
        String name = clazz.getName();
        int dot = name.lastIndexOf('.');
        if (dot >= 0) {
            return name.substring(dot + 1);
        } else {
            return name;
        }
    }

    S2Container getS2Container() {
        return ymir_.getApplication().getS2Container();
    }
}
