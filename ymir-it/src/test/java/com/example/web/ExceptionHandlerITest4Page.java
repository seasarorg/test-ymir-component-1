package com.example.web;

import org.seasar.ymir.handler.annotation.ExceptionHandler;

public class ExceptionHandlerITest4Page {
    private boolean checked;

    public void _get() {
        throw new NullPointerException();
    }

    public void _get_ok() {
        throw new NullPointerException();
    }

    public void _post() {
        throw new NullPointerException();
    }

    @ExceptionHandler(actionName = "_get.*")
    public void handle(NullPointerException ex) {
        checked = true;
    }

    public boolean isChecked() {
        return checked;
    }
}
