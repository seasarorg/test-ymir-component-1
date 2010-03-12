package org.seasar.ymir.dbflute.util;

import junit.framework.TestCase;

public class DBFluteUtilsTest extends TestCase {
    public void test_camelize() throws Exception {
        assertNull(DBFluteUtils.camelize(null));

        assertEquals("a", DBFluteUtils.camelize("a"));

        assertEquals("aBcd", DBFluteUtils.camelize("a_bcd"));

        assertEquals("abcDefGhi", DBFluteUtils.camelize("abc_def_ghi"));

        assertEquals("abcDefGhi", DBFluteUtils.camelize("ABC_DEF_GHI"));
    }
}
