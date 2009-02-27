package org.seasar.ymir.impl;

import org.seasar.ymir.Response;
import org.seasar.ymir.handler.annotation.ExceptionHandler;
import org.seasar.ymir.response.PassthroughResponse;

public class ValidationFailedExceptionHandler {
    @ExceptionHandler
    public String handle() {
        return "redirect:path";
    }
}
