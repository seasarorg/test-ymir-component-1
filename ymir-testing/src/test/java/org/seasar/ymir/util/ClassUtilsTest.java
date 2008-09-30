package org.seasar.ymir.util;

import java.lang.reflect.Method;

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

    public void testGetMethods() throws Exception {
        Method[] actual = ClassUtils.getMethods(SubHoe.class);

        boolean method2Exists = false;
        boolean methodObjectStringExists = false;
        boolean methodStringStringExists = false;
        boolean methodStringObjectExists = false;
        for (int i = 0; i < actual.length; i++) {
            if (actual[i].getName().equals("method")
                    && actual[i].getParameterTypes()[0] == String.class
                    && actual[i].getReturnType() == String.class) {
                methodStringStringExists = true;
            } else if (actual[i].getName().equals("method")
                    && actual[i].getParameterTypes()[0] == String.class
                    && actual[i].getReturnType() == Object.class) {
                methodObjectStringExists = true;
            } else if (actual[i].getName().equals("method")
                    && actual[i].getParameterTypes()[0] == Object.class
                    && actual[i].getReturnType() == String.class) {
                methodStringObjectExists = true;
            } else if (actual[i].getName().equals("method2")) {
                method2Exists = true;
            }
        }
        assertTrue(method2Exists);
        assertFalse(methodObjectStringExists);
        assertTrue(methodStringObjectExists);
        assertTrue(methodStringStringExists);
    }

    public void testIsCovariantOverriddenMethod() throws Exception {
        assertTrue(ClassUtils.isCovariantOverriddenMethod(Hoe.class.getMethod(
                "method", new Class[] { String.class }), SubHoe.class));

        assertFalse(ClassUtils.isCovariantOverriddenMethod(Hoe.class.getMethod(
                "method", new Class[] { Object.class }), SubHoe.class));

        assertFalse(ClassUtils.isCovariantOverriddenMethod(Hoe.class.getMethod(
                "method2", new Class[] { String.class }), SubHoe.class));
    }
}
