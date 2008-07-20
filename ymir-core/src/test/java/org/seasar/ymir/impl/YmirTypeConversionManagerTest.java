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

    public void testConvert_boolean関連の変換が正しく行なわれること() throws Exception {
        assertEquals(Integer.valueOf(1), target_.convert("true", Integer.TYPE));

        assertEquals(Integer.valueOf(0), target_.convert("false", Integer.TYPE));

        assertEquals(Integer.valueOf(1), target_.convert("true", Integer.class));

        assertEquals(Integer.valueOf(0), target_
                .convert("false", Integer.class));

        assertEquals(Boolean.TRUE, target_.convert("1", Boolean.TYPE));

        assertEquals(Boolean.FALSE, target_.convert("0", Boolean.TYPE));

        assertEquals(Boolean.TRUE, target_.convert("1", Boolean.class));

        assertEquals(Boolean.FALSE, target_.convert("0", Boolean.class));
    }
}
