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
                "java.util.List<java.lang.String>");

        assertEquals("java.util.List<String>", target.getName());
    }

    public void testTranscript() throws Exception {
        TypeDescImpl target = new TypeDescImpl("java.util.List<String>");

        TypeDescImpl actual = new TypeDescImpl();
        actual.transcript(target);

        assertEquals("java.util.List<String>", actual.getName());
    }

    public void testGetInitialValue() throws Exception {
        String actual = new TypeDescImpl("com.example.dto.TestDto[]")
                .getInitialValue();
        assertEquals("new com.example.dto.TestDto[0]", actual);

        actual = new TypeDescImpl("java.lang.Integer[]").getInitialValue();
        assertEquals("new Integer[0]", actual);

        actual = new TypeDescImpl("int[]").getInitialValue();
        assertEquals("new int[0]", actual);

        actual = new TypeDescImpl("com.example.dto.TestDto").getInitialValue();
        assertEquals("new com.example.dto.TestDto()", actual);

        actual = new TypeDescImpl("net.skirnir.freyja.render.html.OptionTag")
                .getInitialValue();
        assertEquals("new net.skirnir.freyja.render.html.OptionTag()", actual);

        actual = new TypeDescImpl("com.example.converter.HoeConverter")
                .getInitialValue();
        assertNull(actual);
    }
}
