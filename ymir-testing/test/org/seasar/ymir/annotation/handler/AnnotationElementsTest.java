package org.seasar.ymir.annotation.handler;

import junit.framework.TestCase;

public class AnnotationElementsTest extends TestCase {
    public void testGetPropertyNames() throws Exception {
        String[] actual = AnnotationElements.getPropertyNames(Hoe.class);

        assertEquals(3, actual.length);
        int idx = 0;
        assertEquals("abc", actual[idx++]);
        assertEquals("value", actual[idx++]);
        assertEquals("zzz", actual[idx++]);
    }
}
