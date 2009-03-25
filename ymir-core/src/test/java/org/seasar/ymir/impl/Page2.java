package org.seasar.ymir.impl;

import org.seasar.ymir.Response;
import org.seasar.ymir.handler.annotation.ExceptionHandler;
import org.seasar.ymir.response.PassthroughResponse;

public class Page2 {
    public void _get() {
    }

    @ExceptionHandler(NullPointerException.class)
    public String handle(NullPointerException ex) {
        return "redirect:page2NPE";
    }

    @ExceptionHandler
    public Response handle(Throwable t) {
        return new PassthroughResponse();
    }

    @ExceptionHandler(value = NullPointerException.class, actionName = "_get")
    public String handleGet(NullPointerException t) {
        return "redirect:page2NPE_get";
    }
}
