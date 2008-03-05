package org.seasar.ymir.impl;

import junit.framework.TestCase;

public class YmirTypeConversionManagerTest extends TestCase {
    private YmirTypeConversionManager target_ = new YmirTypeConversionManager();

    public void testConvert() throws Exception {
        assertEquals(Integer.valueOf(0), target_.convert((String) null,
                Integer.TYPE));

        assertEquals(Integer.valueOf(0), target_.convert("", Integer.TYPE));

        assertEquals(Integer.valueOf(0), target_.convert("hoe", Integer.TYPE));

        assertEquals(null, target_.convert((String) null, Integer.class));

        assertEquals(null, target_.convert("", Integer.class));

        assertEquals(null, target_.convert("hoe", Integer.class));
    }
}
