package com.example.web;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Request;
import org.seasar.ymir.annotation.In;
import org.seasar.ymir.scope.impl.ComponentScope;

public class ComponentScopeTestPage {
    private Request request_;

    @In(ComponentScope.class)
    @Binding(bindingType = BindingType.NONE)
    public void setRequest(Request request) {
        request_ = request;
    }

    public void _get() {
    }

    public Request getRequest() {
        return request_;
    }
}
