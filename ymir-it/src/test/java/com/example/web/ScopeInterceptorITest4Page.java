package com.example.web;

import org.seasar.ymir.scope.annotation.PathInfo;

public class ScopeInterceptorITest4Page {
    private String pathInfo_;

    public void _get(@PathInfo
    String pathInfo) {
        pathInfo_ = pathInfo;
    }

    public String getPathInfo() {
        return pathInfo_;
    }
}
