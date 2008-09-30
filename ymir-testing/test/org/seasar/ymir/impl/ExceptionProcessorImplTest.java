package org.seasar.ymir.impl;

import junit.framework.TestCase;

public class ExceptionProcessorImplTest extends TestCase {
    private ExceptionProcessorImpl target_ = new ExceptionProcessorImpl();

    public void testGetComponentName() throws Exception {
        assertEquals("hoeExceptionHandler", target_
                .getComponentName(HoeException.class));
    }

    public void testGetComponentName2() throws Exception {
        assertEquals("URLExceptionHandler", target_
                .getComponentName(URLException.class));
    }
}
