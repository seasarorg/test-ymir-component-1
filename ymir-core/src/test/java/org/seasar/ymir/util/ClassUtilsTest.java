package org.seasar.ymir.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

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

    public void test_isJavaLang_Class() throws Exception {
        assertTrue(ClassUtils.isJavaLang(Object.class));

        assertFalse(ClassUtils.isJavaLang(Map.class));

        assertFalse(ClassUtils.isJavaLang(Array.class));
    }

    public void test_isPrimitive() throws Exception {
        assertFalse(ClassUtils.isPrimitive(null));
        assertFalse(ClassUtils.isPrimitive("java.lang.Integer"));
        assertTrue(ClassUtils.isPrimitive("boolean"));
        assertTrue(ClassUtils.isPrimitive("char"));
        assertTrue(ClassUtils.isPrimitive("byte"));
        assertTrue(ClassUtils.isPrimitive("short"));
        assertTrue(ClassUtils.isPrimitive("int"));
        assertTrue(ClassUtils.isPrimitive("long"));
        assertTrue(ClassUtils.isPrimitive("float"));
        assertTrue(ClassUtils.isPrimitive("double"));
    }

    public void test_getPrimitive() throws Exception {
        assertNull(ClassUtils.getPrimitive(null));
        assertNull(ClassUtils.getPrimitive("java.lang.Integer"));
        assertEquals(Boolean.TYPE, ClassUtils.getPrimitive("boolean"));
        assertEquals(Character.TYPE, ClassUtils.getPrimitive("char"));
        assertEquals(Byte.TYPE, ClassUtils.getPrimitive("byte"));
        assertEquals(Short.TYPE, ClassUtils.getPrimitive("short"));
        assertEquals(Integer.TYPE, ClassUtils.getPrimitive("int"));
        assertEquals(Long.TYPE, ClassUtils.getPrimitive("long"));
        assertEquals(Float.TYPE, ClassUtils.getPrimitive("float"));
        assertEquals(Double.TYPE, ClassUtils.getPrimitive("double"));
    }

    public void test_toWrapperClass() throws Exception {
        assertNull(ClassUtils.toWrapper(null));
        assertNull(ClassUtils.toWrapper(Integer.class));
        assertEquals(Boolean.class, ClassUtils.toWrapper(Boolean.TYPE));
        assertEquals(Character.class, ClassUtils.toWrapper(Character.TYPE));
        assertEquals(Byte.class, ClassUtils.toWrapper(Byte.TYPE));
        assertEquals(Short.class, ClassUtils.toWrapper(Short.TYPE));
        assertEquals(Integer.class, ClassUtils.toWrapper(Integer.TYPE));
        assertEquals(Long.class, ClassUtils.toWrapper(Long.TYPE));
        assertEquals(Float.class, ClassUtils.toWrapper(Float.TYPE));
        assertEquals(Double.class, ClassUtils.toWrapper(Double.TYPE));
    }

    public void test_isWrapper_Class() throws Exception {
        assertFalse(ClassUtils.isWrapper((Class<?>) null));
        assertFalse(ClassUtils.isWrapper(Integer.TYPE));
        assertTrue(ClassUtils.isWrapper(Boolean.class));
        assertTrue(ClassUtils.isWrapper(Character.class));
        assertTrue(ClassUtils.isWrapper(Byte.class));
        assertTrue(ClassUtils.isWrapper(Short.class));
        assertTrue(ClassUtils.isWrapper(Integer.class));
        assertTrue(ClassUtils.isWrapper(Long.class));
        assertTrue(ClassUtils.isWrapper(Float.class));
        assertTrue(ClassUtils.isWrapper(Double.class));
    }

    public void test_isWrapper_String() throws Exception {
        assertFalse(ClassUtils.isWrapper((String) null));
        assertFalse(ClassUtils.isWrapper("int"));
        assertTrue(ClassUtils.isWrapper(Boolean.class.getName()));
        assertTrue(ClassUtils.isWrapper(Character.class.getName()));
        assertTrue(ClassUtils.isWrapper(Byte.class.getName()));
        assertTrue(ClassUtils.isWrapper(Short.class.getName()));
        assertTrue(ClassUtils.isWrapper(Integer.class.getName()));
        assertTrue(ClassUtils.isWrapper(Long.class.getName()));
        assertTrue(ClassUtils.isWrapper(Float.class.getName()));
        assertTrue(ClassUtils.isWrapper(Double.class.getName()));
    }

    public void test_toPrimitiveClass() throws Exception {
        assertNull(ClassUtils.toPrimitive(null));
        assertNull(ClassUtils.toPrimitive(Integer.TYPE));
        assertEquals(Boolean.TYPE, ClassUtils.toPrimitive(Boolean.class));
        assertEquals(Character.TYPE, ClassUtils.toPrimitive(Character.class));
        assertEquals(Byte.TYPE, ClassUtils.toPrimitive(Byte.class));
        assertEquals(Short.TYPE, ClassUtils.toPrimitive(Short.class));
        assertEquals(Integer.TYPE, ClassUtils.toPrimitive(Integer.class));
        assertEquals(Long.TYPE, ClassUtils.toPrimitive(Long.class));
        assertEquals(Float.TYPE, ClassUtils.toPrimitive(Float.class));
        assertEquals(Double.TYPE, ClassUtils.toPrimitive(Double.class));
    }

    public void test_isCapable() throws Exception {
        assertFalse(ClassUtils.isCapable(null, Void.TYPE));

        assertTrue(ClassUtils.isCapable(null, Integer.class));
        assertFalse(ClassUtils.isCapable(null, Integer.TYPE));

        assertTrue(ClassUtils.isCapable(Integer.valueOf(10), Integer.TYPE));
        assertTrue(ClassUtils
                .isCapable(Short.valueOf((short) 10), Integer.TYPE));
        assertFalse(ClassUtils.isCapable(Long.valueOf(10), Integer.TYPE));

        assertTrue(ClassUtils.isCapable(Integer.valueOf(10), Integer.class));
        assertFalse(ClassUtils.isCapable(Short.valueOf((short) 10),
                Integer.class));
        assertFalse(ClassUtils.isCapable(Long.valueOf(10), Integer.class));
    }
}
