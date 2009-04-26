package com.example.web;

import org.seasar.ymir.scope.annotation.RequestParameter;

public class ScopeInterceptorITest5Page {
    private String param_;

    public void _get(@RequestParameter("param")
    String param) {
        param_ = param;
    }

    public String getParam() {
        return param_;
    }
}
