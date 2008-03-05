package org.seasar.ymir.impl;

import junit.framework.TestCase;

public class BeanUtilsTypeConversionManagerTest extends TestCase {
    private BeanUtilsTypeConversionManager target_ = new BeanUtilsTypeConversionManager();

    public void testConvert() throws Exception {
        assertEquals(Integer.valueOf(0), target_.convert((String) null,
                Integer.TYPE));

        assertEquals(Integer.valueOf(0), target_.convert("", Integer.TYPE));

        assertEquals(Integer.valueOf(0), target_.convert("hoe", Integer.TYPE));

        assertEquals(Integer.valueOf(0), target_.convert((String) null,
                Integer.class));

        assertEquals(Integer.valueOf(0), target_.convert("", Integer.class));

        assertEquals(Integer.valueOf(0), target_.convert("hoe", Integer.class));
    }
}
