package org.seasar.ymir.impl;

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.S2Container;
import org.seasar.ymir.ExceptionProcessor;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.Updater;
import org.seasar.ymir.WrappingRuntimeException;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.handler.ExceptionHandler;
import org.seasar.ymir.response.ForwardResponse;
import org.seasar.ymir.response.constructor.ResponseConstructor;
import org.seasar.ymir.response.constructor.ResponseConstructorSelector;
import org.seasar.ymir.util.BeanUtils;

public class DefaultExceptionProcessor implements ExceptionProcessor {

    private Ymir ymir_;

    private ResponseConstructorSelector responseConstructorSelector_;

    private Updater[] updaters_ = new Updater[0];

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

    public Response process(Request request, Throwable t) {

        if (t instanceof WrappingRuntimeException) {
            t = ((WrappingRuntimeException) t).getCause();
        }

        if (ymir_.isUnderDevelopment()) {
            for (int i = 0; i < updaters_.length; i++) {
                Response response = updaters_[i].updateByException(request, t);
                if (response != null) {
                    return response;
                }
            }
        }

        ExceptionHandler handler = null;
        Class exceptionClass = t.getClass();
        do {
            try {
                handler = (ExceptionHandler) getS2Container().getComponent(
                        getComponentName(exceptionClass));
                break;
            } catch (ComponentNotFoundRuntimeException ignore) {
            }
        } while ((exceptionClass = exceptionClass.getSuperclass()) != Object.class);

        if (handler == null) {
            // 見つからなかった場合はデフォルトのハンドラを探す。
            // こうしているのは、(ExceptionHandler)Creatorで定義したコンポーネントは
            // あらゆるコンポーネント定義よりも優先順位が低くなってしまうため。
            exceptionClass = t.getClass();
            do {
                try {
                    handler = (ExceptionHandler) getS2Container().getComponent(
                            NAMEPREFIX_DEFAULT
                                    + getComponentName(exceptionClass));
                    break;
                } catch (ComponentNotFoundRuntimeException ignore) {
                }
            } while ((exceptionClass = exceptionClass.getSuperclass()) != Object.class);
        }

        Response response = constructResponse(handler.handle(t));
        if (response.getType() == ResponseType.PASSTHROUGH) {
            response = new ForwardResponse(PATH_EXCEPTION_TEMPLATE
                    + getClassShortName(exceptionClass)
                    + SUFFIX_EXCEPTION_TEMPLATE);
        }
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

    String getComponentName(Class clazz) {

        return BeanUtils.changeWithPropertyNameRule(getClassShortName(clazz))
                + SUFFIX_HANDLER;
    }

    String getClassShortName(Class clazz) {

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
