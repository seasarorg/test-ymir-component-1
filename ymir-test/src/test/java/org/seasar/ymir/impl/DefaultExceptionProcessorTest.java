package org.seasar.ymir.impl;

import junit.framework.TestCase;

public class DefaultExceptionProcessorTest extends TestCase {
    private DefaultExceptionProcessor target_ = new DefaultExceptionProcessor();

    public void testGetComponentName() throws Exception {
        assertEquals("hoeExceptionHandler", target_
                .getComponentName(HoeException.class));
    }

    public void testGetComponentName2() throws Exception {
        assertEquals("URLExceptionHandler", target_
                .getComponentName(URLException.class));
    }
}
