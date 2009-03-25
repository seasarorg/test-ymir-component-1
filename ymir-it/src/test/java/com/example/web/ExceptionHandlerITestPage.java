package com.example.web;

import org.seasar.ymir.handler.annotation.ExceptionHandler;

public class ExceptionHandlerITestPage {
    private String id_;

    public void _get_noActionName() {
        throw new NullPointerException();
    }

    @ExceptionHandler
    public void handler1(NullPointerException ex) {
        id_ = "1";
    }

    @ExceptionHandler
    public void handler2(Throwable t) {
        id_ = "2";
    }

    @ExceptionHandler(actionName = "_get_noActionName")
    public void handler3(NullPointerException ex) {
        id_ = "3";
    }

    @ExceptionHandler(actionName = "_get_noActionName")
    public void handler4(Throwable t) {
        id_ = "4";
    }

    public String getId() {
        return id_;
    }
}
