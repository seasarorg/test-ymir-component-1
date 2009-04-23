package org.seasar.ymir.extension.creator.impl;

import java.util.List;
import java.util.TreeSet;

import org.seasar.ymir.Application;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.mock.MockApplication;

import junit.framework.TestCase;

public class TypeDescImplTest extends TestCase {
    private DescPool pool_;

    @Override
    protected void setUp() throws Exception {
        pool_ = DescPool.newInstance(new SourceCreatorImpl() {
            @Override
            public Application getApplication() {
                return new MockApplication();
            }

            @Override
            protected ClassLoader getClassLoader() {
                return getClass().getClassLoader();
            }
        }, null);
    }

    public void testGetDefaultValue() throws Exception {
        String actual = new TypeDescImpl(pool_, "com.example.dto.TestDto[]")
                .getDefaultValue();
        assertEquals("null", actual);

        actual = new TypeDescImpl(pool_, "byte").getDefaultValue();
        assertEquals("0", actual);

        actual = new TypeDescImpl(pool_, "short").getDefaultValue();
        assertEquals("0", actual);

        actual = new TypeDescImpl(pool_, "int").getDefaultValue();
        assertEquals("0", actual);

        actual = new TypeDescImpl(pool_, "long").getDefaultValue();
        assertEquals("0", actual);

        actual = new TypeDescImpl(pool_, "float").getDefaultValue();
        assertEquals("0", actual);

        actual = new TypeDescImpl(pool_, "double").getDefaultValue();
        assertEquals("0.0", actual);

        actual = new TypeDescImpl(pool_, "char").getDefaultValue();
        assertEquals("0", actual);

        actual = new TypeDescImpl(pool_, "boolean").getDefaultValue();
        assertEquals("false", actual);
    }

    public void testGetName() throws Exception {
        TypeDescImpl target = new TypeDescImpl(pool_, "java.lang.Integer");

        assertEquals("Integer", target.getName());
    }

    public void testGetName_Generics対応() throws Exception {
        TypeDescImpl target = new TypeDescImpl(pool_,
                "java.util.List<java.lang.String>");

        assertEquals("java.util.List<String>", target.getName());
    }

    public void testSetName() throws Exception {
        TypeDescImpl target = new TypeDescImpl(pool_, "java.lang.Date");

        assertEquals("java.lang.Date", target.getComponentClassDesc().getName());
        assertFalse(target.isCollection());
        assertNull(target.getCollectionClassName());
    }

    public void testSetName_配列() throws Exception {
        TypeDescImpl target = new TypeDescImpl(pool_, "java.lang.Date[]");

        assertEquals("java.lang.Date", target.getComponentClassDesc().getName());
        assertTrue(target.isCollection());
        assertNull(target.getCollectionClassName());
    }

    public void testSetName_コレクション() throws Exception {
        TypeDescImpl target = new TypeDescImpl(pool_,
                "java.util.List<java.lang.Date>");

        assertEquals("java.lang.Date", target.getComponentClassDesc().getName());
        assertTrue(target.isCollection());
        assertEquals("java.util.List", target.getCollectionClassName());
    }

    public void testSetName_コレクションの配列() throws Exception {
        TypeDescImpl target = new TypeDescImpl(pool_,
                "java.util.List<java.lang.Date>[]");

        assertEquals("java.util.List", target.getComponentClassDesc().getName());
        assertTrue(target.isCollection());
        assertNull(target.getCollectionClassName());
    }

    public void testSetName_コレクションでないGenerics() throws Exception {
        TypeDescImpl target = new TypeDescImpl(pool_,
                "java.lang.ThreadLocal<java.lang.Date>");

        assertEquals("java.lang.ThreadLocal", target.getComponentClassDesc()
                .getName());
        assertFalse(target.isCollection());
        assertNull(target.getCollectionClassName());
    }

    public void testGetName_コレクション() throws Exception {
        TypeDescImpl target = new TypeDescImpl(pool_, "");
        target.setCollection(true);
        target.setCollectionClassName("java.util.List");
        target.setComponentClassDesc(String.class);

        assertEquals("java.util.List<String>", target.getName());
    }

    public void testGetCompleteName_コレクション() throws Exception {
        TypeDescImpl target = new TypeDescImpl(pool_, "");
        target.setCollection(true);
        target.setCollectionClassName("java.util.List");
        target.setComponentClassDesc(String.class);

        assertEquals("java.util.List<java.lang.String>", target
                .getCompleteName());
    }

    public void test_addDependingClassNamesTo() throws Exception {
        TypeDescImpl target = new TypeDescImpl(pool_, "java.util.List<"
                + ParameterDescImplTest.class.getName() + ">");

        TreeSet<String> set = new TreeSet<String>();

        target.addDependingClassNamesTo(set);

        String[] actual = set.toArray(new String[0]);
        assertEquals(2, actual.length);
        int idx = 0;
        assertEquals(List.class.getName(), actual[idx++]);
        assertEquals(ParameterDescImplTest.class.getName(), actual[idx++]);
    }
}
