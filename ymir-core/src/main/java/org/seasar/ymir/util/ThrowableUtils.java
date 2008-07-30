package org.seasar.ymir.util;

import org.seasar.ymir.WrappingRuntimeException;

public class ThrowableUtils {
    protected ThrowableUtils() {
    }

    public static Throwable unwrap(Throwable t) {
        if (!(t instanceof WrappingRuntimeException)) {
            return t;
        }
        return unwrap(t.getCause());
    }
}
