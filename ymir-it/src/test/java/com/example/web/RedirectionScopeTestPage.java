package com.example.web;

import org.seasar.ymir.redirection.impl.RedirectionScope;
import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Out;

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

    public void _get_redirect() {
    }
}
