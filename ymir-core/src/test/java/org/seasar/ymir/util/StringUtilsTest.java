package org.seasar.ymir.util;

import junit.framework.TestCase;

public class StringUtilsTest extends TestCase {
    public void testUnique() throws Exception {
        assertNull(StringUtils.unique((String[]) null));

        String[] actual = StringUtils.unique("a", "b", "a");
        assertEquals(2, actual.length);
        int idx = 0;
        assertEquals("a", actual[idx++]);
        assertEquals("b", actual[idx++]);
    }
}
