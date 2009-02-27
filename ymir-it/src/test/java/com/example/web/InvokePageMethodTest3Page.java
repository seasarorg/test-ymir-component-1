package com.example.web;

import org.seasar.ymir.Phase;
import org.seasar.ymir.annotation.Invoke;

public class InvokePageMethodTest3Page {
    @Invoke(Phase.PAGECOMPONENT_CREATED)
    public void invoke() {
    }

    public String _get() {
        return "redirect:path";
    }
}
