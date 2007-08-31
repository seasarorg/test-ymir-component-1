package com.example.web;

import org.seasar.ymir.annotation.In;
import org.seasar.ymir.annotation.Out;
import org.seasar.ymir.scope.impl.ApplicationScope;

public class ScopePage {
    private String param1_;

    private String param2_;

    @In(scopeClass = ApplicationScope.class, actionName = "_post")
    public void setParam1(String param1) {
        param1_ = param1;
    }

    @In(scopeClass = ApplicationScope.class)
    public void setParam2(String param2) {
        param2_ = param2;
    }

    @Out(scopeClass = ApplicationScope.class, actionName = "_post")
    public String getParam1() {
        return param1_;
    }

    @Out(scopeClass = ApplicationScope.class)
    public String getParam2() {
        return param2_;
    }

    public void _get() {
    }

    public void _post() {
    }
}
