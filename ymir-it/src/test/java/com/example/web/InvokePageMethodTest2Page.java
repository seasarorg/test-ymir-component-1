package com.example.web;

import org.seasar.ymir.Phase;
import org.seasar.ymir.annotation.Invoke;
import org.seasar.ymir.scope.annotation.Resolve;
import org.seasar.ymir.scope.impl.ApplicationScope;

public class InvokePageMethodTest2Page {
    private String value_;

    @Invoke(Phase.PAGECOMPONENT_CREATED)
    public void invoke(
            @Resolve(scopeClass = ApplicationScope.class, name = "value")
            String value) {
        value_ = value;
    }

    public void _get() {
    }

    public String getValue() {
        return value_;
    }
}
