package org.seasar.ymir.util;

import junit.framework.TestCase;

public class ClassUtilsTest extends TestCase {
    public void testNewInstanceFromAbstractClass() throws Exception {
        AbstractHoe actual = null;
        try {
            actual = ClassUtils.newInstanceFromAbstractClass(AbstractHoe.class);
        } catch (Exception ex) {
            fail();
        }
        assertNotNull(actual);
        assertEquals("value", actual.getValue());
        try {
            actual.getAbstractValue();
            fail();
        } catch (AbstractMethodError expected) {
        }
    }
}
