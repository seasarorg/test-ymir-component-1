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
        assertEquals("0.0", actual);

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

    public void testSetName() throws Exception {
        TypeDescImpl target = new TypeDescImpl();
        target.setName("java.lang.Date");

        assertEquals("java.lang.Date", target.getComponentClassDesc().getName());
        assertFalse(target.isCollection());
        assertNull(target.getCollectionClassName());
    }

    public void testSetName_配列() throws Exception {
        TypeDescImpl target = new TypeDescImpl();
        target.setName("java.lang.Date[]");

        assertEquals("java.lang.Date", target.getComponentClassDesc().getName());
        assertTrue(target.isCollection());
        assertNull(target.getCollectionClassName());
    }

    public void testSetName_コレクション() throws Exception {
        TypeDescImpl target = new TypeDescImpl();
        target.setName("java.util.List<java.lang.Date>");

        assertEquals("java.lang.Date", target.getComponentClassDesc().getName());
        assertTrue(target.isCollection());
        assertEquals("java.util.List", target.getCollectionClassName());
    }

    public void testSetName_コレクションの配列() throws Exception {
        TypeDescImpl target = new TypeDescImpl();
        target.setName("java.util.List<java.lang.Date>[]");

        assertEquals("java.util.List", target.getComponentClassDesc().getName());
        assertTrue(target.isCollection());
        assertNull(target.getCollectionClassName());
    }

    public void testSetName_コレクションでないGenerics() throws Exception {
        TypeDescImpl target = new TypeDescImpl();
        target.setName("java.lang.ThreadLocal<java.lang.Date>");

        assertEquals("java.lang.ThreadLocal", target.getComponentClassDesc()
                .getName());
        assertFalse(target.isCollection());
        assertNull(target.getCollectionClassName());
    }

    public void testGetName_コレクション() throws Exception {
        TypeDescImpl target = new TypeDescImpl();
        target.setCollection(true);
        target.setCollectionClassName("java.util.List");
        target.setComponentClassDesc(new ClassDescImpl("java.lang.String"));

        assertEquals("java.util.List<String>", target.getName());
    }

    public void testGetCompleteName_コレクション() throws Exception {
        TypeDescImpl target = new TypeDescImpl();
        target.setCollection(true);
        target.setCollectionClassName("java.util.List");
        target.setComponentClassDesc(new ClassDescImpl("java.lang.String"));

        assertEquals("java.util.List<java.lang.String>", target
                .getCompleteName());
    }
}
