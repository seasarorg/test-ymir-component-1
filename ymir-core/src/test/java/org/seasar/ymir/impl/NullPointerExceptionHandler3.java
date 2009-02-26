package org.seasar.ymir.impl;

import org.seasar.ymir.Response;
import org.seasar.ymir.handler.ExceptionHandler;
import org.seasar.ymir.response.PassthroughResponse;

@SuppressWarnings("deprecation")
public class NullPointerExceptionHandler3 implements
        ExceptionHandler<NullPointerException> {
    public String handle(NullPointerException ex) {
        return "passthrough:";
    }
}
