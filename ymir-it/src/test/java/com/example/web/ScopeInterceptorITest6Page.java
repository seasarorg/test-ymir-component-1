package com.example.web;

import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.scope.annotation.Inject;

public class ScopeInterceptorITest6Page {
    private ScopeManager scopeManager_;

    public void _get(@Inject
    ScopeManager scopeManager) {
        scopeManager_ = scopeManager;
    }

    public ScopeManager getScopeManager() {
        return scopeManager_;
    }
}
