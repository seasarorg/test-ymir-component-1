package com.example.web;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.PageITestComponent1;
import org.seasar.ymir.PageITestComponent2;
import org.seasar.ymir.PageITestComponent3;
import org.seasar.ymir.scope.annotation.Inject;

public class PageITestPage {
    private PageITestComponent1 component1_;

    private PageITestComponent2 component2_;

    private PageITestComponent3 component3_;

    public PageITestComponent1 getComponent1() {
        return component1_;
    }

    public void setComponent1(PageITestComponent1 component1) {
        component1_ = component1;
    }

    public PageITestComponent2 getComponent2() {
        return component2_;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setComponent2(PageITestComponent2 component2) {
        component2_ = component2;
    }

    public PageITestComponent3 getComponent3() {
        return component3_;
    }

    @Inject
    public void setComponent3(PageITestComponent3 component3) {
        component3_ = component3;
    }

    public void _get() {
    }
}
