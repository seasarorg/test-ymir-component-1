package com.example.web;

import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.RequestParameter;
import org.seasar.ymir.scope.impl.RequestScope;

public class ScopeInterceptorITest8Page {
    private Object object_;

    public void _get(@In(scopeClass = RequestScope.class, name = "name")
    @RequestParameter("param")
    Object object) {
        object_ = object;
    }

    public Object getObject() {
        return object_;
    }
}
