package com.example.web;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.scope.annotation.Out;
import org.seasar.ymir.window.WindowManager;
import org.seasar.ymir.window.impl.WindowScope;

public class WindowScopeTestPage {
    private String windowId_;

    @Binding(bindingType = BindingType.MUST)
    protected WindowManager windowManager_;

    @Out(WindowScope.class)
    public String getOutjectedValue() {
        return "OUTJECTED_VALUE";
    }

    public void _get() {
    }

    public void _get_button1() {
        windowId_ = windowManager_.getWindowId();
    }

    public void _post_button2() {
        windowId_ = windowManager_.getWindowId();
    }

    public String getWindowId() {
        return windowId_;
    }
}
