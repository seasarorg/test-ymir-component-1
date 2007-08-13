package com.example.web;

import org.seasar.ymir.annotation.In;
import org.seasar.ymir.annotation.Out;
import org.seasar.ymir.redirection.impl.RedirectionScope;

public class RedirectionScopeTestPage {
    private String injectedValue_;

    private String value_;

    @In(RedirectionScope.class)
    public void setValue(String value) {
        injectedValue_ = value;
    }

    public String getInjectedValue() {
        return injectedValue_;
    }

    @Out(RedirectionScope.class)
    public String getValue() {
        return value_;
    }

    public String _get() {
        value_ = "INJECTED_VALUE";
        return "redirect:.";
    }
}
