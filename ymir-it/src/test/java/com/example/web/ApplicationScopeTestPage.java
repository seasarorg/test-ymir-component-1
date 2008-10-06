package com.example.web;

import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Out;
import org.seasar.ymir.scope.impl.ApplicationScope;

public class ApplicationScopeTestPage {
    private String injectedValue_;

    @In(ApplicationScope.class)
    public void setInjectedValue(String value) {
        injectedValue_ = value;
    }

    public String getInjectedValue() {
        return injectedValue_;
    }

    @Out(ApplicationScope.class)
    public String getOutjectedValue() {
        return "OUTJECTED_VALUE";
    }

    public void _get() {
    }
}
