package org.seasar.ymir.converter.impl;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.ComponentClientTestCase;
import org.seasar.ymir.PropertyHandler;

public class TypeConversionManagerImplTest extends ComponentClientTestCase {
    private S2Container container_;

    private TypeConversionManagerImpl target_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        target_ = getComponent(TypeConversionManagerImpl.class);
    }

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

    public void testConvert_Object_Class_isArray_typeIsNotArray_長さ0_Byte()
            throws Exception {
        Byte actual = target_.convert(new Byte[0], Byte.class);

        assertNull(actual);
    }

    public void testConvert_Object_Class_isArray_typeIsNotArray_長さ0_Boolean()
            throws Exception {
        Boolean actual = target_.convert(new Boolean[0], Boolean.class);

        assertNull(actual);
    }

    public void testConvert_Object_Class_isArray_typeIsNotArray_長さ0_Character()
            throws Exception {
        Character actual = target_.convert(new Character[0], Character.class);

        assertNull(actual);
    }

    public void testConvert_Object_Class_isArray_typeIsNotArray_長さ0_Double()
            throws Exception {
        Double actual = target_.convert(new Double[0], Double.class);

        assertNull(actual);
    }

    public void testConvert_Object_Class_isArray_typeIsNotArray_長さ0_Float()
            throws Exception {
        Float actual = target_.convert(new Float[0], Float.class);

        assertNull(actual);
    }

    public void testConvert_Object_Class_isArray_typeIsNotArray_長さ0_Integer()
            throws Exception {
        Integer actual = target_.convert(new Integer[0], Integer.class);

        assertNull(actual);
    }

    public void testConvert_Object_Class_isArray_typeIsNotArray_長さ0_Long()
            throws Exception {
        Long actual = target_.convert(new Long[0], Long.class);

        assertNull(actual);
    }

    public void testConvert_Object_Class_isArray_typeIsNotArray_長さ0_Short()
            throws Exception {
        Short actual = target_.convert(new Short[0], Short.class);

        assertNull(actual);
    }

    public void testConvert_Object_Class_isArray_typeIsNotArray_長さ0_boolean()
            throws Exception {
        Boolean actual = target_.convert(new Boolean[0], Boolean.TYPE);

        assertFalse(actual.booleanValue());
    }

    public void testConvert_Object_Class_isArray_typeIsNotArray_長さ0_byte()
            throws Exception {
        Byte actual = target_.convert(new Byte[0], Byte.TYPE);

        assertEquals(Byte.valueOf((byte) 0), actual);
    }

    public void testConvert_Object_Class_isArray_typeIsNotArray_長さ0_char()
            throws Exception {
        Character actual = target_.convert(new Byte[0], Character.TYPE);

        assertEquals('\0', actual.charValue());
    }

    public void testConvert_Object_Class_isArray_typeIsNotArray_長さ0_double()
            throws Exception {
        Double actual = target_.convert(new Byte[0], Double.TYPE);

        assertEquals(0D, actual.doubleValue());
    }

    public void testConvert_Object_Class_isArray_typeIsNotArray_長さ0_float()
            throws Exception {
        Float actual = target_.convert(new Byte[0], Float.TYPE);

        assertEquals(0F, actual.floatValue());
    }

    public void testConvert_Object_Class_isArray_typeIsNotArray_長さ0_int()
            throws Exception {
        Integer actual = target_.convert(new Byte[0], Integer.TYPE);

        assertEquals(0, actual.intValue());
    }

    public void testConvert_Object_Class_isArray_typeIsNotArray_長さ0_long()
            throws Exception {
        Long actual = target_.convert(new Byte[0], Long.TYPE);

        assertEquals(0L, actual.longValue());
    }

    public void testConvert_Object_Class_isArray_typeIsNotArray_長さ0_short()
            throws Exception {
        Short actual = target_.convert(new Byte[0], Short.TYPE);

        assertEquals((short) 0, actual.shortValue());
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

    public void testConvert() throws Exception {
        assertEquals("nullの場合", Integer.valueOf(0), target_.convert(null,
                Integer.TYPE));

        assertEquals("Stringの場合", Integer.valueOf(1), target_.convert("1",
                Integer.TYPE));

        assertEquals("castできる場合", Integer.valueOf(1), target_.convert(Integer
                .valueOf(1), Number.class));

        assertEquals("castできない場合", Byte.valueOf((byte) 1), target_.convert(
                Integer.valueOf(1), Byte.class));
    }

    public void testGetPropertyHandler() throws Exception {
        Aaa aaa = new Aaa();

        PropertyHandler actual = target_.getPropertyHandler(aaa, "bbb.ccc");

        assertNotNull(actual);
        actual.setProperty("value");
        assertEquals("value", aaa.getBbb().getCcc());
    }

    public static class Aaa {
        private Bbb bbb_ = new Bbb();

        public Bbb getBbb() {
            return bbb_;
        }

        public void setBbb(Bbb bbb) {
            bbb_ = bbb;
        }
    }

    public static class Bbb {
        private String ccc_;

        public String getCcc() {
            return ccc_;
        }

        public void setCcc(String ccc) {
            ccc_ = ccc;
        }
    }
}
