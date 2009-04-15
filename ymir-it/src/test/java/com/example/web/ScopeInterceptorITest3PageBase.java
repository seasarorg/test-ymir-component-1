package com.example.web;

import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Out;
import org.seasar.ymir.scope.impl.SessionScope;

public class ScopeInterceptorITest3PageBase {
    protected String test;

    @In(SessionScope.class)
    public void setTest(String test) {
        this.test = test;
    }

    @Out(SessionScope.class)
    public String getTest() {
        return test;
    }
}
