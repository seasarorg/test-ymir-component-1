package org.seasar.ymir.util;

import junit.framework.TestCase;

public class LogUtilsTest extends TestCase {
    public void testToString() throws Exception {
        assertEquals("[a, b]", LogUtils.toString(new Object[] { "a", "b" }));
        assertEquals("[1, 2]", LogUtils.toString(new int[] { 1, 2 }));
    }
}
