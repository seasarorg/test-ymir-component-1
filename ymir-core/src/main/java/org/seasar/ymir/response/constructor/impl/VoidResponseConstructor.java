package org.seasar.ymir.response.constructor.impl;

import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Response;
import org.seasar.ymir.response.PassthroughResponse;
import org.seasar.ymir.response.VoidResponse;
import org.seasar.ymir.response.constructor.ResponseConstructor;

public class VoidResponseConstructor implements ResponseConstructor<Void> {
    private S2Container container_;

    @Binding(bindingType = BindingType.MUST)
    public void setContainer(S2Container container) {
        container_ = container;
    }

    public Class<Void> getTargetClass() {
        return Void.TYPE;
    }

    public Response constructResponse(Object page, Void returnValue) {
        if (getHttpServletResponse().isCommitted()) {
            return VoidResponse.INSTANCE;
        } else {
            return new PassthroughResponse();
        }
    }

    private HttpServletResponse getHttpServletResponse() {
        return (HttpServletResponse) container_
                .getComponent(HttpServletResponse.class);
    }
}
