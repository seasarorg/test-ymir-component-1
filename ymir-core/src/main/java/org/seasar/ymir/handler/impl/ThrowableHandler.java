package org.seasar.ymir.handler.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.ymir.WrappingRuntimeException;
import org.seasar.ymir.handler.annotation.ExceptionHandler;

public class ThrowableHandler {
    private static final Log log_ = LogFactory.getLog(ThrowableHandler.class);

    @ExceptionHandler
    public String handle(Throwable t) {
        log_.error("Exception has occured", t);

        if (t instanceof Error) {
            throw (Error) t;
        } else if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        } else {
            throw new WrappingRuntimeException(t);
        }
    }
}
