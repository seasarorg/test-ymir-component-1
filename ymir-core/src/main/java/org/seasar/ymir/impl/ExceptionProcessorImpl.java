package org.seasar.ymir.impl;

import java.beans.Introspector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ComponentMetaDataFactory;
import org.seasar.ymir.ExceptionProcessor;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.Updater;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.handler.ExceptionHandler;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;
import org.seasar.ymir.response.ForwardResponse;
import org.seasar.ymir.response.constructor.ResponseConstructor;
import org.seasar.ymir.response.constructor.ResponseConstructorSelector;
import org.seasar.ymir.util.ThrowableUtils;
import org.seasar.ymir.util.YmirUtils;

public class ExceptionProcessorImpl implements ExceptionProcessor {
    private Ymir ymir_;

    private ComponentMetaDataFactory componentMetaDataFactory_;

    private ResponseConstructorSelector responseConstructorSelector_;

    private Updater[] updaters_ = new Updater[0];

    private YmirProcessInterceptor[] ymirProcessInterceptors_ = new YmirProcessInterceptor[0];

    private final Log log_ = LogFactory.getLog(ExceptionProcessorImpl.class);

    @Binding(bindingType = BindingType.MUST)
    public void setYmir(Ymir ymir) {
        ymir_ = ymir;
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

        S2Container container = getS2Container();
        ComponentDef handlerCd = null;
        Class<?> exceptionClass = t.getClass();
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

        final ExceptionHandler<? extends Throwable> originalHandler = (ExceptionHandler<? extends Throwable>) handlerCd
                .getComponent();
        if (log_.isDebugEnabled()) {
            log_.debug("Raw ExceptionHandler: " + originalHandler);
        }

        ExceptionHandler<? extends Throwable> handler = originalHandler;
        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
            handler = ymirProcessInterceptors_[i].exceptionHandlerInvoking(
                    originalHandler, handler);
        }
        if (log_.isDebugEnabled()) {
            log_.debug("Final ExceptionHandler: " + handler);
        }

        Response response = constructResponse(((ExceptionHandler<Throwable>) handler)
                .handle(t));
        if (log_.isDebugEnabled()) {
            log_.debug("Raw response: " + response);
        }

        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
            response = ymirProcessInterceptors_[i]
                    .responseCreatedByExceptionHandler(handler, response);
        }

        if (response.getType() == ResponseType.PASSTHROUGH) {
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

    Response constructResponse(String returnValue) {
        ResponseConstructor<String> constructor = responseConstructorSelector_
                .getResponseConstructor(String.class);
        if (constructor == null) {
            throw new ComponentNotFoundRuntimeException(
                    "Can't find ResponseConstructor for type '" + String.class
                            + "' in ResponseConstructorSelector");
        }

        return constructor.constructResponse(null, returnValue);
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
