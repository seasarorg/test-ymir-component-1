package org.seasar.ymir.impl;

import org.seasar.ymir.handler.ExceptionHandler;

@SuppressWarnings("deprecation")
public class Page1 implements ExceptionHandler<NullPointerException> {
    public String handle(NullPointerException t) {
        return null;
    }
}
