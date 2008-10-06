package com.example.web;

import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Ins;
import org.seasar.ymir.scope.annotation.Out;
import org.seasar.ymir.scope.annotation.Outs;
import org.seasar.ymir.scope.impl.ApplicationScope;

public class ScopePage {
    private String param1_;

    private String param2_;

    private String param3_;

    @In(scopeClass = ApplicationScope.class, actionName = "_post")
    public void setParam1(String param1) {
        param1_ = param1;
    }

    @In(scopeClass = ApplicationScope.class)
    public void setParam2(String param2) {
        param2_ = param2;
    }

    @Ins(@In(scopeClass = ApplicationScope.class))
    public void setParam3(String param3) {
        param3_ = param3;
    }

    @Out(scopeClass = ApplicationScope.class, actionName = "_post")
    public String getParam1() {
        return param1_;
    }

    @Out(scopeClass = ApplicationScope.class)
    public String getParam2() {
        return param2_;
    }

    @Outs(@Out(scopeClass = ApplicationScope.class))
    public String getParam3() {
        return param3_;
    }

    public void _get() {
    }

    public void _post() {
    }
}
