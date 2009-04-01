package com.example.web;

import org.seasar.ymir.Phase;
import org.seasar.ymir.annotation.Invoke;
import org.seasar.ymir.handler.annotation.ExceptionHandler;
import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.impl.SessionScope;

public class ExceptionHandlerITest2Page {
    private String message;

    @Invoke(Phase.PAGECOMPONENT_CREATED)
    public void initialize() {
        throw new NullPointerException();
    }

    @In(SessionScope.class)
    public void setMessage(String message) {
        this.message = message;
    }

    public void _get() {
    }

    @ExceptionHandler
    public void handle(NullPointerException ex) {
    }

    public String getMessage() {
        return message;
    }
}
