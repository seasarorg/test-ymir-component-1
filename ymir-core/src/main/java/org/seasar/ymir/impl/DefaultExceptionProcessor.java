package org.seasar.ymir.impl;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ExceptionProcessor;
import org.seasar.ymir.PageProcessor;
import org.seasar.ymir.PageMetaData;
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

    private PageProcessor pageProcessor_;

    private ResponseConstructorSelector responseConstructorSelector_;

    private Updater[] updaters_ = new Updater[0];

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

        ExceptionHandler handler = (ExceptionHandler) handlerCd.getComponent();

        // 各コンテキストが持つ属性をinjectする。
        PageMetaData pageMetaData = new PageMetaDataImpl(handlerCd
                .getComponentClass(), container);
        // actionNameはExceptionがスローされたタイミングで未決定であったり決定できていたりする。
        // そういう不確定な情報に頼るのはよろしくないので敢えてnullとみなすようにしている。
        pageProcessor_.injectContextAttributes(handler, pageMetaData, null);

        Response response = constructResponse(handler.handle(t));
        if (response.getType() == ResponseType.PASSTHROUGH) {
            response = new ForwardResponse(PATH_EXCEPTION_TEMPLATE
                    + getClassShortName(exceptionClass)
                    + SUFFIX_EXCEPTION_TEMPLATE);
        }

        // 各コンテキストに属性をoutjectする。
        pageProcessor_.outjectContextAttributes(handler, pageMetaData, null);

        // ExceptionHandlerコンポーネントをattributeとしてバインドしておく。
        request.setAttribute(ATTR_HANDLER, handler);

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
