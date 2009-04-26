package com.example.web;

import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Ins;
import org.seasar.ymir.scope.impl.RequestParameterScope;
import org.seasar.ymir.scope.impl.RequestScope;

public class ScopeInterceptorITest7Page {
    private Object object_;

    public void _get(@Ins( {
        @In(scopeClass = RequestScope.class, name = "object"),
        @In(scopeClass = RequestParameterScope.class, name = "param") })
    Object object) {
        object_ = object;
    }

    public Object getObject() {
        return object_;
    }
}
