package com.example.web;

import org.seasar.ymir.scope.annotation.Populate;
import org.seasar.ymir.scope.impl.RequestParameterScope;

public class ScopeAttributePopulatorTest2Page {
    private String a_;

    private String aaa_;

    private String bbb_;

    public String getA() {
        return a_;
    }

    @Populate(RequestParameterScope.class)
    public void setA(String a) {
        a_ = a;
    }

    public String getAaa() {
        return aaa_;
    }

    @Populate(scopeClass = RequestParameterScope.class, name = "a")
    public void setAaa(String aaa) {
        aaa_ = aaa;
    }

    public String getBbb() {
        return bbb_;
    }

    @Populate(scopeClass = RequestParameterScope.class, name = "b")
    public void setBbb(String bbb) {
        bbb_ = bbb;
    }

    public void _get() {
    }
}
