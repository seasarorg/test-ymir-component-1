package com.example.web;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.AppDiconComponent;

public class HoePage {
    private AppDiconComponent appDiconComponent_;

    public AppDiconComponent getAppDiconComponent() {
        return appDiconComponent_;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setAppDiconComponent(AppDiconComponent appDiconComponent) {
        appDiconComponent_ = appDiconComponent;
    }

    public void _get() {
    }
}
