package org.seasar.ymir.extension.creator.impl;

import junit.framework.TestCase;

public class TypeDescImplTest extends TestCase {
    public void testGetDefaultValue() throws Exception {
        String actual = new TypeDescImpl("com.example.dto.TestDto[]")
                .getDefaultValue();
        assertEquals("null", actual);

        actual = new TypeDescImpl("byte").getDefaultValue();
        assertEquals("0", actual);

        actual = new TypeDescImpl("short").getDefaultValue();
        assertEquals("0", actual);

        actual = new TypeDescImpl("int").getDefaultValue();
        assertEquals("0", actual);

        actual = new TypeDescImpl("long").getDefaultValue();
        assertEquals("0", actual);

        actual = new TypeDescImpl("float").getDefaultValue();
        assertEquals("0", actual);

        actual = new TypeDescImpl("double").getDefaultValue();
        assertEquals("0", actual);

        actual = new TypeDescImpl("char").getDefaultValue();
        assertEquals("0", actual);

        actual = new TypeDescImpl("boolean").getDefaultValue();
        assertEquals("false", actual);
    }

    public void testGetName() throws Exception {
        TypeDescImpl target = new TypeDescImpl("java.lang.Integer");

        assertEquals("Integer", target.getName());
    }

    public void testGetName_Generics対応() throws Exception {
        TypeDescImpl target = new TypeDescImpl(
                "java.lang.List<java.lang.String>");

        assertEquals("List<String>", target.getName());
    }
}
