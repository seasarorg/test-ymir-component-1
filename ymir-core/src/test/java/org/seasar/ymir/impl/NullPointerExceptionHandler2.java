package org.seasar.ymir.impl;

import org.seasar.ymir.Response;
import org.seasar.ymir.handler.annotation.ExceptionHandler;
import org.seasar.ymir.response.RedirectResponse;

public class NullPointerExceptionHandler2 {
    @ExceptionHandler
    public Response handle(NullPointerException ex) {
        return new RedirectResponse("path");
    }
}
