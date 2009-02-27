package com.example.web;

import org.seasar.ymir.Phase;
import org.seasar.ymir.annotation.Invoke;

public class InvokePageMethodTest4Page {
    private boolean invoked_;

    @Invoke(Phase.PAGECOMPONENT_CREATED)
    public String invoke() {
        return "path";
    }

    public String _get() {
        invoked_ = true;
        return "redirect:path";
    }

    public boolean isInvoked() {
        return invoked_;
    }
}
