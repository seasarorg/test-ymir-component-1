package com.example.web;

import org.seasar.ymir.handler.annotation.ExceptionHandler;
import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.impl.SessionScope;

public class ExceptionHandlerITest3IncludedPage {
    private String message;

    @In(SessionScope.class)
    public void setMessage(String message) {
        this.message = message;
    }

    @ExceptionHandler
    public void handle(NullPointerException ex) {
    }

    public String getMessage() {
        return message;
    }
}
