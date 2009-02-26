package org.seasar.ymir.impl;

import org.seasar.ymir.Response;
import org.seasar.ymir.handler.annotation.ExceptionHandler;
import org.seasar.ymir.response.PassthroughResponse;
import org.seasar.ymir.response.RedirectResponse;

public class NullPointerExceptionHandler3 {
    @ExceptionHandler
    public Response handle(NullPointerException ex) {
        return new PassthroughResponse();
    }
}
