package com.example.web;

import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Out;
import org.seasar.ymir.scope.annotation.Resolve;
import org.seasar.ymir.scope.impl.ApplicationScope;
import org.seasar.ymir.scope.impl.RequestParameterScope;

@SuppressWarnings("deprecation")
public class ScopeInterceptorITestPage {
    private String value_;

    @In(scopeClass = RequestParameterScope.class, actionName = "_get")
    public void setValue(String value) {
        value_ = value;
    }

    public void _get() {
    }

    public void _get_resolve(
            @In(scopeClass = RequestParameterScope.class, name = "value")
            String value) {
        value_ = value;
    }

    public void _get_resolve2(
            @Resolve(scopeClass = RequestParameterScope.class, name = "value")
            String value) {
        value_ = value;
    }

    @Out(ApplicationScope.class)
    public String getValue() {
        return value_;
    }
}
