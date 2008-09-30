package org.seasar.ymir.util;

import junit.framework.TestCase;

import org.seasar.ymir.WrappingRuntimeException;

public class ThrowableUtilsTest extends TestCase {
    public void testUnwrap() throws Exception {
        assertNull(ThrowableUtils.unwrap(null));

        Throwable t = new ClassNotFoundException();
        assertSame(t, ThrowableUtils.unwrap(t));

        WrappingRuntimeException wrapped = new WrappingRuntimeException(t);
        assertSame(t, ThrowableUtils.unwrap(wrapped));

        wrapped = new WrappingRuntimeException(wrapped);
        assertSame(t, ThrowableUtils.unwrap(wrapped));
    }
}
