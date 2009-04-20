package org.seasar.ymir.extension.creator.impl;

import junit.framework.TestCase;

public class TypeDescImplTest extends TestCase {
    public void testGetDefaultValue() throws Exception {
        String actual = new TypeDescImpl(null, "com.example.dto.TestDto[]")
                .getDefaultValue();
        assertEquals("null", actual);

        actual = new TypeDescImpl(null, "byte").getDefaultValue();
        assertEquals("0", actual);

        actual = new TypeDescImpl(null, "short").getDefaultValue();
        assertEquals("0", actual);

        actual = new TypeDescImpl(null, "int").getDefaultValue();
        assertEquals("0", actual);

        actual = new TypeDescImpl(null, "long").getDefaultValue();
        assertEquals("0", actual);

        actual = new TypeDescImpl(null, "float").getDefaultValue();
        assertEquals("0", actual);

        actual = new TypeDescImpl(null, "double").getDefaultValue();
        assertEquals("0.0", actual);

        actual = new TypeDescImpl(null, "char").getDefaultValue();
        assertEquals("0", actual);

        actual = new TypeDescImpl(null, "boolean").getDefaultValue();
        assertEquals("false", actual);
    }

    public void testGetName() throws Exception {
        TypeDescImpl target = new TypeDescImpl(null, "java.lang.Integer");

        assertEquals("Integer", target.getName());
    }

    public void testGetName_Generics対応() throws Exception {
        TypeDescImpl target = new TypeDescImpl(null,
                "java.util.List<java.lang.String>");

        assertEquals("java.util.List<String>", target.getName());
    }

    public void testSetName() throws Exception {
        TypeDescImpl target = new TypeDescImpl(null, "java.lang.Date");

        assertEquals("java.lang.Date", target.getComponentClassDesc().getName());
        assertFalse(target.isCollection());
        assertNull(target.getCollectionClassName());
    }

    public void testSetName_配列() throws Exception {
        TypeDescImpl target = new TypeDescImpl(null, "java.lang.Date[]");

        assertEquals("java.lang.Date", target.getComponentClassDesc().getName());
        assertTrue(target.isCollection());
        assertNull(target.getCollectionClassName());
    }

    public void testSetName_コレクション() throws Exception {
        TypeDescImpl target = new TypeDescImpl(null,
                "java.util.List<java.lang.Date>");

        assertEquals("java.lang.Date", target.getComponentClassDesc().getName());
        assertTrue(target.isCollection());
        assertEquals("java.util.List", target.getCollectionClassName());
    }

    public void testSetName_コレクションの配列() throws Exception {
        TypeDescImpl target = new TypeDescImpl(null,
                "java.util.List<java.lang.Date>[]");

        assertEquals("java.util.List", target.getComponentClassDesc().getName());
        assertTrue(target.isCollection());
        assertNull(target.getCollectionClassName());
    }

    public void testSetName_コレクションでないGenerics() throws Exception {
        TypeDescImpl target = new TypeDescImpl(null,
                "java.lang.ThreadLocal<java.lang.Date>");

        assertEquals("java.lang.ThreadLocal", target.getComponentClassDesc()
                .getName());
        assertFalse(target.isCollection());
        assertNull(target.getCollectionClassName());
    }

    public void testGetName_コレクション() throws Exception {
        TypeDescImpl target = new TypeDescImpl(null, "");
        target.setCollection(true);
        target.setCollectionClassName("java.util.List");
        target.setComponentClassDesc(String.class);

        assertEquals("java.util.List<String>", target.getName());
    }

    public void testGetCompleteName_コレクション() throws Exception {
        TypeDescImpl target = new TypeDescImpl(null, "");
        target.setCollection(true);
        target.setCollectionClassName("java.util.List");
        target.setComponentClassDesc(String.class);

        assertEquals("java.util.List<java.lang.String>", target
                .getCompleteName());
    }
}
