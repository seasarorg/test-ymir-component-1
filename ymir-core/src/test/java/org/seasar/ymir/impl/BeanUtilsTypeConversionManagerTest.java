package org.seasar.ymir.impl;

import junit.framework.TestCase;

public class BeanUtilsTypeConversionManagerTest extends TestCase {
    private BeanUtilsTypeConversionManager target_ = new BeanUtilsTypeConversionManager();

    public void testConvert_Object_Class_valueがnullの場合() throws Exception {
        assertEquals(Integer.valueOf(0), target_.convert((Object) null,
                Integer.TYPE));
    }

    public void testConvert_Object_Class_isArray_typeIsArray() throws Exception {
        Byte[] actual = target_.convert(new Integer[] { Integer.valueOf(1),
            Integer.valueOf(2) }, Byte[].class);

        assertEquals(2, actual.length);
        int idx = 0;
        assertEquals(Byte.valueOf((byte) 1), actual[idx++]);
        assertEquals(Byte.valueOf((byte) 2), actual[idx++]);
    }

    public void testConvert_Object_Class_isArray_typeIsNotArray_長さ1以上()
            throws Exception {
        Byte actual = target_.convert(new Integer[] { Integer.valueOf(1),
            Integer.valueOf(2) }, Byte.class);

        assertEquals(Byte.valueOf((byte) 1), actual);
    }

    public void testConvert_Object_Class_isArray_typeIsNotArray_長さ0()
            throws Exception {
        Byte actual = target_.convert(new Integer[0], Byte.class);

        assertEquals(Byte.valueOf((byte) 0), actual);
    }

    public void testConvert_Object_Class_isNotArray_typeIsArray()
            throws Exception {
        Byte[] actual = target_.convert(Integer.valueOf(1), Byte[].class);

        assertEquals(1, actual.length);
        int idx = 0;
        assertEquals(Byte.valueOf((byte) 1), actual[idx++]);
    }

    public void testConvert_Object_Class_isNotArray_typeIsNotArray()
            throws Exception {
        Byte actual = target_.convert(Integer.valueOf(1), Byte.class);

        assertEquals(Byte.valueOf((byte) 1), actual);
    }

    public void testConvertComponent() throws Exception {
        assertEquals("nullの場合", Integer.valueOf(0), target_.convertComponent(
                (Object) null, Integer.TYPE));

        assertEquals("Stringの場合", Integer.valueOf(1), target_.convertComponent(
                "1", Integer.TYPE));

        assertEquals("castできる場合", Integer.valueOf(1), target_.convertComponent(
                Integer.valueOf(1), Number.class));

        assertEquals("castできない場合", Byte.valueOf((byte) 1), target_
                .convertComponent(Integer.valueOf(1), Byte.class));
    }
}
