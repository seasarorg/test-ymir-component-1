package org.seasar.ymir.extension.creator.util;

import junit.framework.TestCase;

public class DescUtilsTest extends TestCase {
    public void testGetNonGenericClassName() throws Exception {
        assertNull(DescUtils.getNonGenericClassName(null));

        assertEquals("java.lang.List", DescUtils
                .getNonGenericClassName("java.lang.List"));

        assertEquals("java.lang.List", DescUtils
                .getNonGenericClassName("java.lang.List<String>"));

        assertEquals("Iterator", DescUtils
                .getNonGenericClassName("Iterator<Map.Entry<String, String>>"));
    }
}
