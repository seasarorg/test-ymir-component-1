package com.example.web;

import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Out;
import org.seasar.ymir.scope.impl.ApplicationScope;
import org.seasar.ymir.scope.impl.RequestParameterScope;

public class ScopeInterceptorITestPage {
    private String value_;

    @In(RequestParameterScope.class)
    public void setValue(String value) {
        value_ = value;
    }

    public void _get() {
    }

    @Out(ApplicationScope.class)
    public String getValue() {
        return value_;
    }
}
