package org.seasar.ymir.impl;

import junit.framework.TestCase;

public class TypeConversionManagerImplTest extends TestCase {
    private TypeConversionManagerImpl target_ = new TypeConversionManagerImpl();

    public void testConvert() throws Exception {
        assertEquals(Integer.valueOf(0), target_.convert(null, Integer.TYPE));

        assertEquals(Integer.valueOf(0), target_.convert("", Integer.TYPE));

        assertEquals(Integer.valueOf(0), target_.convert("hoe", Integer.TYPE));

        assertEquals(Integer.valueOf(0), target_.convert(null, Integer.class));

        assertEquals(Integer.valueOf(0), target_.convert("", Integer.class));

        assertEquals(Integer.valueOf(0), target_.convert("hoe", Integer.class));
    }
}
