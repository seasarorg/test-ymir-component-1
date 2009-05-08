package org.seasar.ymir.extension.creator.impl;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.seasar.ymir.extension.Globals;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.render.html.Option;

public class PropertyDescImplTest extends SourceCreatorImplTestBase {
    private DescPool pool_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        pool_ = DescPool.newInstance(target_, null);
    }

    public void testGetGetterName() throws Exception {
        PropertyDescImpl target = new PropertyDescImpl(pool_, "value");
        target.setTypeDesc(Boolean.TYPE);
        assertEquals("isValue", target.getGetterName());
    }

    public void testGetGetterName2() throws Exception {
        PropertyDescImpl target = new PropertyDescImpl(pool_, "value");
        target.setTypeDesc(String.class);
        assertEquals("getValue", target.getGetterName());
    }

    public void testGetGetterName3() throws Exception {
        PropertyDescImpl target = new PropertyDescImpl(pool_, "value");
        target.setTypeDesc(Boolean.TYPE);
        target.setGetterName("getValue");
        assertEquals("getValue", target.getGetterName());
    }

    public void testGetInitialValue() throws Exception {
        PropertyDescImpl pd = new PropertyDescImpl(pool_, "name");
        pd.setTypeDesc(new TypeDescImpl(pool_, "com.example.dto.TestDto[]"));
        String actual = pd.getInitialValue();
        assertEquals("new com.example.dto.TestDto[0]", actual);

        pd.setTypeDesc(new TypeDescImpl(pool_, "java.lang.Integer[]"));
        actual = pd.getInitialValue();
        assertEquals("new Integer[0]", actual);

        pd.setTypeDesc(new TypeDescImpl(pool_, "int[]"));
        actual = pd.getInitialValue();
        assertEquals("new int[0]", actual);

        pd.setTypeDesc(new TypeDescImpl(pool_, "com.example.dto.TestDto"));
        actual = pd.getInitialValue();
        assertEquals("Dtoが存在しない場合はnew記述になること", "new com.example.dto.TestDto()",
                actual);

        pd.setTypeDesc(new TypeDescImpl(pool_, "com.example.dto.Test1Dto"));
        actual = pd.getInitialValue();
        assertEquals("デフォルトコンストラクタでインスタンスを生成できる場合はnew記述になること",
                "new com.example.dto.Test1Dto()", actual);

        pd.setTypeDesc(new TypeDescImpl(pool_, "com.example.dto.Test2Dto"));
        actual = pd.getInitialValue();
        assertNull("デフォルトコンストラクタがないばあいはnullになること", actual);

        pd.setTypeDesc(new TypeDescImpl(pool_, Option.class.getName()));
        actual = pd.getInitialValue();
        assertEquals("new " + Option.class.getName() + "()", actual);

        pd.setTypeDesc(new TypeDescImpl(pool_,
                "com.example.converter.HoeConverter"));
        actual = pd.getInitialValue();
        assertNull(actual);
    }

    public void test_addDependingClassNamesTo() throws Exception {
        PropertyDescImpl target = new PropertyDescImpl(pool_, "name");
        target.setTypeDesc(new TypeDescImpl(pool_, "java.util.List<"
                + PropertyDescImplTest.class.getName() + ">"));
        target.setAnnotationDesc(new AnnotationDescImpl("org.example.Noe",
                "value"));
        target.setAnnotationDescOnGetter(new AnnotationDescImpl(
                "org.example.Hoe", "value"));
        target.setAnnotationDescOnSetter(new AnnotationDescImpl(
                "org.example.Foe", "value"));

        TreeSet<String> set = new TreeSet<String>();

        target.addDependingClassNamesTo(set);

        String[] actual = set.toArray(new String[0]);
        assertEquals(5, actual.length);
        int idx = 0;
        assertEquals(List.class.getName(), actual[idx++]);
        assertEquals("org.example.Foe", actual[idx++]);
        assertEquals("org.example.Hoe", actual[idx++]);
        assertEquals("org.example.Noe", actual[idx++]);
        assertEquals(PropertyDescImplTest.class.getName(), actual[idx++]);
    }

    public void test_Collection型の場合は実装型を取得できること() throws Exception {
        PropertyDescriptor listDescriptor = null;
        for (PropertyDescriptor descriptor : Introspector.getBeanInfo(
                Hoe4.class).getPropertyDescriptors()) {
            if ("list".equals(descriptor.getName())) {
                listDescriptor = descriptor;
                break;
            }
        }

        PropertyDescImpl target = new PropertyDescImpl(pool_, listDescriptor);

        assertEquals(ArrayList.class.getName(), target.getTypeDesc()
                .getCollectionImplementationClassName());
    }

    public void testRemoveBornOf1() throws Exception {
        PropertyDesc target = new PropertyDescImpl(pool_, "property");
        target.setMode(PropertyDesc.READ | PropertyDesc.WRITE);
        target.setAnnotationDesc(new MetaAnnotationDescImpl(
                Globals.META_NAME_BORNOF, new String[] { "a", "b" }));
        target.setAnnotationDescOnGetter(new MetaAnnotationDescImpl(
                Globals.META_NAME_BORNOF, new String[] { "a", "b" }));
        target.setAnnotationDescOnSetter(new MetaAnnotationDescImpl(
                Globals.META_NAME_BORNOF, new String[] { "a" }));

        assertFalse(target.removeBornOf("a"));

        assertEquals(PropertyDesc.READ, target.getMode());

        String[] values = target.getMetaValue(Globals.META_NAME_BORNOF);
        assertNotNull(values);
        assertEquals(1, values.length);
        assertEquals("b", values[0]);

        values = target.getMetaValueOnGetter(Globals.META_NAME_BORNOF);
        assertNotNull(values);
        assertEquals(1, values.length);
        assertEquals("b", values[0]);

        values = target.getMetaValueOnSetter(Globals.META_NAME_BORNOF);
        assertNull(values);
    }

    public void testRemoveBornOf2() throws Exception {
        PropertyDesc target = new PropertyDescImpl(pool_, "property");
        target.setMode(PropertyDesc.READ | PropertyDesc.WRITE);
        target.setAnnotationDesc(new MetaAnnotationDescImpl(
                Globals.META_NAME_BORNOF, new String[] { "a", "b" }));
        target.setAnnotationDescOnGetter(new MetaAnnotationDescImpl(
                Globals.META_NAME_BORNOF, new String[] { "a" }));
        target.setAnnotationDescOnSetter(new MetaAnnotationDescImpl(
                Globals.META_NAME_BORNOF, new String[] { "a" }));

        assertFalse(target.removeBornOf("a"));

        assertEquals(PropertyDesc.NONE, target.getMode());

        String[] values = target.getMetaValue(Globals.META_NAME_BORNOF);
        assertNotNull(values);
        assertEquals(1, values.length);
        assertEquals("b", values[0]);

        values = target.getMetaValueOnGetter(Globals.META_NAME_BORNOF);
        assertNull(values);

        values = target.getMetaValueOnSetter(Globals.META_NAME_BORNOF);
        assertNull(values);
    }

    public void testRemoveBornOf3() throws Exception {
        PropertyDesc target = new PropertyDescImpl(pool_, "property");
        target.setMode(PropertyDesc.READ | PropertyDesc.WRITE);
        target.setAnnotationDesc(new MetaAnnotationDescImpl(
                Globals.META_NAME_BORNOF, new String[] { "a" }));
        target.setAnnotationDescOnGetter(new MetaAnnotationDescImpl(
                Globals.META_NAME_BORNOF, new String[] { "a" }));
        target.setAnnotationDescOnSetter(new MetaAnnotationDescImpl(
                Globals.META_NAME_BORNOF, new String[] { "a" }));

        assertTrue(target.removeBornOf("a"));
    }

    public void testRemoveBornOf4() throws Exception {
        PropertyDesc target = new PropertyDescImpl(pool_, "property");
        target.setMode(PropertyDesc.NONE);
        target.setAnnotationDesc(new MetaAnnotationDescImpl(
                Globals.META_NAME_BORNOF, new String[] { "a", "b" }));

        assertFalse(target.removeBornOf("a"));

        assertEquals(PropertyDesc.NONE, target.getMode());

        String[] values = target.getMetaValue(Globals.META_NAME_BORNOF);
        assertNotNull(values);
        assertEquals(1, values.length);
        assertEquals("b", values[0]);

        values = target.getMetaValueOnGetter(Globals.META_NAME_BORNOF);
        assertNull(values);

        values = target.getMetaValueOnSetter(Globals.META_NAME_BORNOF);
        assertNull(values);
    }

    public void testRemoveBornOf5() throws Exception {
        PropertyDesc target = new PropertyDescImpl(pool_, "property");
        target.setMode(PropertyDesc.NONE);
        target.setAnnotationDesc(new MetaAnnotationDescImpl(
                Globals.META_NAME_BORNOF, new String[] { "a" }));

        assertTrue(target.removeBornOf("a"));
    }

    public void test_getInitialShortValue() throws Exception {
        PropertyDescImpl pd = new PropertyDescImpl(pool_, "name");
        Set<String> set = new TreeSet<String>();
        pd.setTouchedClassNameSet(set);

        pd.setTypeDesc(new TypeDescImpl(pool_, "com.example.dto.TestDto[]"));
        String actual = pd.getInitialShortValue();
        assertEquals("new TestDto[0]", actual);
        assertTrue(set.contains("com.example.dto.TestDto"));

        set.clear();
        pd.setTypeDesc(new TypeDescImpl(pool_, "java.lang.Integer[]"));
        actual = pd.getInitialShortValue();
        assertEquals("new Integer[0]", actual);
        assertTrue(set.contains("java.lang.Integer"));

        set.clear();
        pd.setTypeDesc(new TypeDescImpl(pool_, "int[]"));
        actual = pd.getInitialShortValue();
        assertEquals("new int[0]", actual);

        set.clear();
        pd.setTypeDesc(new TypeDescImpl(pool_, "com.example.dto.TestDto"));
        actual = pd.getInitialShortValue();
        assertEquals("Dtoが存在しない場合はnew記述になること", "new TestDto()", actual);
        assertTrue(set.contains("com.example.dto.TestDto"));

        set.clear();
        pd.setTypeDesc(new TypeDescImpl(pool_, "com.example.dto.Test1Dto"));
        actual = pd.getInitialShortValue();
        assertEquals("デフォルトコンストラクタでインスタンスを生成できる場合はnew記述になること",
                "new Test1Dto()", actual);
        assertTrue(set.contains("com.example.dto.Test1Dto"));

        set.clear();
        pd.setTypeDesc(new TypeDescImpl(pool_, "com.example.dto.Test2Dto"));
        actual = pd.getInitialShortValue();
        assertNull("デフォルトコンストラクタがないばあいはnullになること", actual);
        assertFalse(set.contains("com.example.dto.Test2Dto"));

        set.clear();
        pd.setTypeDesc(new TypeDescImpl(pool_, Option.class.getName()));
        actual = pd.getInitialShortValue();
        assertEquals("new Option()", actual);
        assertTrue(set.contains(Option.class.getName()));

        set.clear();
        pd.setTypeDesc(new TypeDescImpl(pool_,
                "com.example.converter.HoeConverter"));
        actual = pd.getInitialShortValue();
        assertNull(actual);
        assertFalse(set.contains("com.example.converter.HoeConverter"));
    }
}
