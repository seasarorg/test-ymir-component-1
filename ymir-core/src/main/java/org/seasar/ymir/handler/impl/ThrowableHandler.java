package org.seasar.ymir.handler.impl;

import org.seasar.ymir.WrappingRuntimeException;
import org.seasar.ymir.handler.ExceptionHandler;

public class ThrowableHandler implements ExceptionHandler<Throwable> {
    public String handle(Throwable t) {

        if (t instanceof Error) {
            throw (Error) t;
        } else if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        } else {
            throw new WrappingRuntimeException(t);
        }
    }
}
