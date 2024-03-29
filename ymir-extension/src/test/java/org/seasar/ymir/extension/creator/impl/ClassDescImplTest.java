package org.seasar.ymir.extension.creator.impl;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;

import com.example.page.TestPageBase;

public class ClassDescImplTest extends SourceCreatorImplTestBase {
    private DescPool pool_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        pool_ = DescPool.newInstance(target_, null);
    }

    public void testGetInstanceName() throws Exception {
        String actual = new ClassDescImpl(pool_, "com.example.dto.TestDto")
                .getInstanceName();
        assertEquals("testDto", actual);
    }

    public void testGetInstanceName_プリミティブ型の場合() throws Exception {
        String actual = new ClassDescImpl(pool_, "int").getInstanceName();
        assertEquals("intValue", actual);
    }

    public void testMerge() throws Exception {
        ClassDesc actual = new ClassDescImpl(pool_, "com.example.page.TestPage");
        PropertyDesc pd = new PropertyDescImpl(pool_, "param1");
        pd.setMode(PropertyDesc.READ);
        actual.setPropertyDesc(pd);
        pd = new PropertyDescImpl(pool_, "param2");
        pd.setMode(PropertyDesc.WRITE);
        actual.setPropertyDesc(pd);
        pd = new PropertyDescImpl(pool_, "param3");
        pd.setTypeDesc(String.class);
        pd.notifyTypeUpdated(PropertyDesc.PROBABILITY_MAXIMUM);
        actual.setPropertyDesc(pd);
        pd = new PropertyDescImpl(pool_, "param4");
        pd.setTypeDesc(String[].class);
        actual.setPropertyDesc(pd);
        pd = new PropertyDescImpl(pool_, "param6");
        pd.setTypeDesc(Integer.class);
        actual.setPropertyDesc(pd);
        pd = new PropertyDescImpl(pool_, "param7");
        pd.setTypeDesc(String.class);
        actual.setPropertyDesc(pd);
        MethodDesc md = new MethodDescImpl(pool_, "method");
        md.setReturnTypeDesc("java.lang.String");
        actual.setMethodDesc(md);
        md = new MethodDescImpl(pool_, "method2");
        md.setReturnTypeDesc(String.class);
        md.getReturnTypeDesc().setExplicit(true);
        actual.setMethodDesc(md);
        md = new MethodDescImpl(pool_, "method3");
        md.setReturnTypeDesc(String.class);
        md.getReturnTypeDesc().setExplicit(true);
        actual.setMethodDesc(md);
        md = new MethodDescImpl(pool_, "method4");
        md.setReturnTypeDesc("java.lang.String");
        actual.setMethodDesc(md);
        md = new MethodDescImpl(pool_, "method5");
        md.setReturnTypeDesc("java.lang.String");
        md.setAnnotationDesc(new MetaAnnotationDescImpl("a", new String[0],
                new Class[0]));
        md.setAnnotationDesc(new AnnotationDescImpl("name1"));
        actual.setMethodDesc(md);

        ClassDesc cd2 = new ClassDescImpl(pool_, "com.example.page.TestPage");
        cd2.setSuperclassName(TestPageBase.class.getName());
        pd = new PropertyDescImpl(pool_, "param1");
        pd.setTypeDesc(Integer.class);
        pd.notifyTypeUpdated(PropertyDesc.PROBABILITY_MAXIMUM);
        pd.setMode(PropertyDesc.WRITE);
        cd2.setPropertyDesc(pd);
        pd = new PropertyDescImpl(pool_, "param2");
        pd.setTypeDesc(Integer.class);
        pd.notifyTypeUpdated(PropertyDesc.PROBABILITY_MAXIMUM);
        pd.setMode(PropertyDesc.READ);
        cd2.setPropertyDesc(pd);
        pd = new PropertyDescImpl(pool_, "param3");
        pd.setTypeDesc(Integer.class);
        pd.notifyTypeUpdated(PropertyDesc.PROBABILITY_MAXIMUM);
        cd2.setPropertyDesc(pd);
        pd = new PropertyDescImpl(pool_, "param5");
        pd.setTypeDesc(Integer[].class);
        pd.notifyTypeUpdated(PropertyDesc.PROBABILITY_MAXIMUM);
        cd2.setPropertyDesc(pd);
        pd = new PropertyDescImpl(pool_, "param7");
        pd.setTypeDesc(Integer.class);
        cd2.setPropertyDesc(pd);
        md = new MethodDescImpl(pool_, "method");
        md.setReturnTypeDesc(Integer.class);
        md.getReturnTypeDesc().setExplicit(true);
        md.setBodyDesc(new BodyDescImpl("body", new String[0]));
        cd2.setMethodDesc(md);
        md = new MethodDescImpl(pool_, "method2");
        md.setReturnTypeDesc("java.lang.Integer");
        md.setBodyDesc(new BodyDescImpl("body", new String[0]));
        cd2.setMethodDesc(md);
        md = new MethodDescImpl(pool_, "method3");
        md.setReturnTypeDesc(Integer.class);
        md.getReturnTypeDesc().setExplicit(true);
        md.setBodyDesc(new BodyDescImpl("body", new String[0]));
        cd2.setMethodDesc(md);
        md = new MethodDescImpl(pool_, "method4");
        md.setReturnTypeDesc("java.lang.Integer");
        md.setBodyDesc(new BodyDescImpl("body", new String[0]));
        cd2.setMethodDesc(md);
        md = new MethodDescImpl(pool_, "method5");
        md.setReturnTypeDesc("java.lang.String");
        md.setAnnotationDesc(new MetaAnnotationDescImpl("b", new String[0],
                new Class[0]));
        md.setAnnotationDesc(new AnnotationDescImpl("name2"));
        cd2.setMethodDesc(md);

        actual.merge(cd2, false);

        assertEquals("com.example.page.TestPageBase", actual
                .getSuperclassName());
        assertEquals(7, actual.getPropertyDescs().length);
        assertEquals(PropertyDesc.READ | PropertyDesc.WRITE, actual
                .getPropertyDesc("param1").getMode());
        assertEquals("Integer", actual.getPropertyDesc("param1").getTypeDesc()
                .getName());
        assertEquals(PropertyDesc.READ | PropertyDesc.WRITE, actual
                .getPropertyDesc("param2").getMode());
        assertEquals("Integer", actual.getPropertyDesc("param2").getTypeDesc()
                .getName());
        assertEquals("プロパティのtypeが両方ともexplicitである場合はもともとのtypeが優先されること",
                "String", actual.getPropertyDesc("param3").getTypeDesc()
                        .getName());
        assertEquals(PropertyDesc.READ | PropertyDesc.WRITE, actual
                .getPropertyDesc("param1").getMode());
        assertTrue(actual.getPropertyDesc("param4").getTypeDesc().getName()
                .endsWith("[]"));
        assertEquals("Integer[]", actual.getPropertyDesc("param5")
                .getTypeDesc().getName());
        assertEquals("Integer", actual.getPropertyDesc("param6").getTypeDesc()
                .getName());
        assertEquals("プロパティのtypeが両方ともimplicitである場合はもともとのtypeが優先されること",
                "String", actual.getPropertyDesc("param7").getTypeDesc()
                        .getName());
        MethodDesc actualMd = actual.getMethodDesc(new MethodDescImpl(pool_,
                "method"));
        assertNotNull(actualMd);
        assertEquals("body", actualMd.getBodyDesc().getRoot().get("body"));
        assertEquals("Integer", actual.getMethodDesc(
                new MethodDescImpl(pool_, "method")).getReturnTypeDesc()
                .getName());
        assertEquals("String", actual.getMethodDesc(
                new MethodDescImpl(pool_, "method2")).getReturnTypeDesc()
                .getName());
        assertEquals("メソッドの返り値のtypeが両方ともexplicitである場合はもともとのtypeが優先されること",
                "String", actual.getMethodDesc(
                        new MethodDescImpl(pool_, "method3"))
                        .getReturnTypeDesc().getName());
        assertEquals("メソッドの返り値のtypeが両方ともimplicitである場合はもともとのtypeが優先されること",
                "String", actual.getMethodDesc(
                        new MethodDescImpl(pool_, "method4"))
                        .getReturnTypeDesc().getName());
        assertNotNull("メソッドのMeta系でないアノテーションはマージされること", actual.getMethodDesc(
                new MethodDescImpl(pool_, "method5"))
                .getAnnotationDesc("name1"));
        assertNotNull("メソッドのMeta系でないアノテーションはマージされること", actual.getMethodDesc(
                new MethodDescImpl(pool_, "method5"))
                .getAnnotationDesc("name2"));
        assertNotNull("メソッドのMeta系アノテーションもマージされること", actual.getMethodDesc(
                new MethodDescImpl(pool_, "method5")).getMetaValue("a"));
        assertNotNull("メソッドのMeta系アノテーションもマージされること", actual.getMethodDesc(
                new MethodDescImpl(pool_, "method5")).getMetaValue("b"));
    }

    public void testMerge2() throws Exception {
        ClassDesc actual = new ClassDescImpl(pool_, "com.example.page.TestPage");
        PropertyDesc pd = new PropertyDescImpl(pool_, "param1");
        pd.setMode(PropertyDesc.READ);
        actual.setPropertyDesc(pd);
        pd = new PropertyDescImpl(pool_, "param2");
        pd.setMode(PropertyDesc.WRITE);
        actual.setPropertyDesc(pd);
        pd = new PropertyDescImpl(pool_, "param3");
        pd.setTypeDesc(String.class);
        pd.notifyTypeUpdated(PropertyDesc.PROBABILITY_MAXIMUM);
        actual.setPropertyDesc(pd);
        pd = new PropertyDescImpl(pool_, "param4");
        pd.setTypeDesc(String[].class);
        actual.setPropertyDesc(pd);
        pd = new PropertyDescImpl(pool_, "param6");
        pd.setTypeDesc(Integer.class);
        actual.setPropertyDesc(pd);
        pd = new PropertyDescImpl(pool_, "param7");
        pd.setTypeDesc(String.class);
        actual.setPropertyDesc(pd);
        MethodDesc md = new MethodDescImpl(pool_, "method");
        md.setReturnTypeDesc("java.lang.String");
        actual.setMethodDesc(md);
        md = new MethodDescImpl(pool_, "method2");
        md.setReturnTypeDesc(String.class);
        md.getReturnTypeDesc().setExplicit(true);
        actual.setMethodDesc(md);
        md = new MethodDescImpl(pool_, "method3");
        md.setReturnTypeDesc(String.class);
        md.getReturnTypeDesc().setExplicit(true);
        actual.setMethodDesc(md);
        md = new MethodDescImpl(pool_, "method4");
        md.setReturnTypeDesc("java.lang.String");
        actual.setMethodDesc(md);
        md = new MethodDescImpl(pool_, "method5");
        md.setReturnTypeDesc("java.lang.String");
        md.setAnnotationDesc(new MetaAnnotationDescImpl("a", new String[0],
                new Class[0]));
        md.setAnnotationDesc(new AnnotationDescImpl("name1"));
        actual.setMethodDesc(md);

        ClassDesc cd2 = new ClassDescImpl(pool_, "com.example.page.TestPage");
        cd2.setSuperclassName(TestPageBase.class.getName());
        pd = new PropertyDescImpl(pool_, "param1");
        pd.setTypeDesc(Integer.class);
        pd.notifyTypeUpdated(PropertyDesc.PROBABILITY_MAXIMUM);
        pd.setMode(PropertyDesc.WRITE);
        cd2.setPropertyDesc(pd);
        pd = new PropertyDescImpl(pool_, "param2");
        pd.setTypeDesc(Integer.class);
        pd.notifyTypeUpdated(PropertyDesc.PROBABILITY_MAXIMUM);
        pd.setMode(PropertyDesc.READ);
        cd2.setPropertyDesc(pd);
        pd = new PropertyDescImpl(pool_, "param3");
        pd.setTypeDesc(Integer.class);
        pd.notifyTypeUpdated(PropertyDesc.PROBABILITY_MAXIMUM);
        cd2.setPropertyDesc(pd);
        pd = new PropertyDescImpl(pool_, "param5");
        pd.setTypeDesc(Integer[].class);
        pd.notifyTypeUpdated(PropertyDesc.PROBABILITY_MAXIMUM);
        cd2.setPropertyDesc(pd);
        pd = new PropertyDescImpl(pool_, "param7");
        pd.setTypeDesc(Integer.class);
        cd2.setPropertyDesc(pd);
        md = new MethodDescImpl(pool_, "method");
        md.setReturnTypeDesc(Integer.class);
        md.getReturnTypeDesc().setExplicit(true);
        md.setBodyDesc(new BodyDescImpl("body", new String[0]));
        cd2.setMethodDesc(md);
        md = new MethodDescImpl(pool_, "method2");
        md.setReturnTypeDesc("java.lang.Integer");
        md.setBodyDesc(new BodyDescImpl("body", new String[0]));
        cd2.setMethodDesc(md);
        md = new MethodDescImpl(pool_, "method3");
        md.setReturnTypeDesc(Integer.class);
        md.getReturnTypeDesc().setExplicit(true);
        md.setBodyDesc(new BodyDescImpl("body", new String[0]));
        cd2.setMethodDesc(md);
        md = new MethodDescImpl(pool_, "method4");
        md.setReturnTypeDesc("java.lang.Integer");
        md.setBodyDesc(new BodyDescImpl("body", new String[0]));
        cd2.setMethodDesc(md);
        md = new MethodDescImpl(pool_, "method5");
        md.setReturnTypeDesc("java.lang.String");
        md.setAnnotationDesc(new MetaAnnotationDescImpl("b", new String[0],
                new Class[0]));
        md.setAnnotationDesc(new AnnotationDescImpl("name2"));
        cd2.setMethodDesc(md);

        actual.merge(cd2, true);

        assertEquals("com.example.page.TestPageBase", actual
                .getSuperclassName());
        assertEquals(7, actual.getPropertyDescs().length);
        assertEquals(PropertyDesc.READ | PropertyDesc.WRITE, actual
                .getPropertyDesc("param1").getMode());
        assertEquals("Integer", actual.getPropertyDesc("param1").getTypeDesc()
                .getName());
        assertEquals(PropertyDesc.READ | PropertyDesc.WRITE, actual
                .getPropertyDesc("param2").getMode());
        assertEquals("Integer", actual.getPropertyDesc("param2").getTypeDesc()
                .getName());
        assertEquals("プロパティのtypeが両方ともexplicitである場合は引数で指定された側のtypeが優先されること",
                "Integer", actual.getPropertyDesc("param3").getTypeDesc()
                        .getName());
        assertEquals(PropertyDesc.READ | PropertyDesc.WRITE, actual
                .getPropertyDesc("param1").getMode());
        assertTrue(actual.getPropertyDesc("param4").getTypeDesc().getName()
                .endsWith("[]"));
        assertEquals("Integer[]", actual.getPropertyDesc("param5")
                .getTypeDesc().getName());
        assertEquals("Integer", actual.getPropertyDesc("param6").getTypeDesc()
                .getName());
        assertEquals("プロパティのtypeが両方ともimplicitである場合は引数で指定された側のtypeが優先されること",
                "Integer", actual.getPropertyDesc("param7").getTypeDesc()
                        .getName());
        MethodDesc actualMd = actual.getMethodDesc(new MethodDescImpl(pool_,
                "method"));
        assertNotNull(actualMd);
        assertEquals("body", actualMd.getBodyDesc().getRoot().get("body"));
        assertEquals("Integer", actual.getMethodDesc(
                new MethodDescImpl(pool_, "method")).getReturnTypeDesc()
                .getName());
        assertEquals("String", actual.getMethodDesc(
                new MethodDescImpl(pool_, "method2")).getReturnTypeDesc()
                .getName());
        assertEquals("メソッドの返り値のtypeが両方ともexplicitである場合は引数で指定された側のtypeが優先されること",
                "Integer", actual.getMethodDesc(
                        new MethodDescImpl(pool_, "method3"))
                        .getReturnTypeDesc().getName());
        assertEquals("メソッドの返り値のtypeが両方ともimplicitである場合は引数で指定された側のtypeが優先されること",
                "Integer", actual.getMethodDesc(
                        new MethodDescImpl(pool_, "method4"))
                        .getReturnTypeDesc().getName());
        assertNotNull("メソッドのMeta系でないアノテーションはマージされること", actual.getMethodDesc(
                new MethodDescImpl(pool_, "method5"))
                .getAnnotationDesc("name1"));
        assertNotNull("メソッドのMeta系でないアノテーションはマージされること", actual.getMethodDesc(
                new MethodDescImpl(pool_, "method5"))
                .getAnnotationDesc("name2"));
        assertNotNull("メソッドのMeta系アノテーションもマージされること", actual.getMethodDesc(
                new MethodDescImpl(pool_, "method5")).getMetaValue("a"));
        assertNotNull("メソッドのMeta系アノテーションもマージされること", actual.getMethodDesc(
                new MethodDescImpl(pool_, "method5")).getMetaValue("b"));
    }

    public void testMerge3_YMIR_221_プロパティのアノテーションがマージされること() throws Exception {
        ClassDesc actual = new ClassDescImpl(pool_, "com.example.page.TestPage");
        PropertyDesc pd = new PropertyDescImpl(pool_, "param1");
        pd.setMode(PropertyDesc.READ);
        pd.setAnnotationDesc(new MetaAnnotationDescImpl("a", new String[0],
                new Class[0]));
        pd.setAnnotationDesc(new AnnotationDescImpl("name1"));
        pd.setAnnotationDescOnGetter(new MetaAnnotationDescImpl("a_getter",
                new String[0], new Class[0]));
        pd.setAnnotationDescOnGetter(new AnnotationDescImpl("name1_getter",
                "body1"));
        pd.setAnnotationDescOnSetter(new MetaAnnotationDescImpl("a_setter",
                new String[0], new Class[0]));
        pd.setAnnotationDescOnSetter(new AnnotationDescImpl("name1_setter"));
        actual.setPropertyDesc(pd);

        ClassDesc cd2 = new ClassDescImpl(pool_, "com.example.page.TestPage");
        cd2.setSuperclassName(TestPageBase.class.getName());
        pd = new PropertyDescImpl(pool_, "param1");
        pd.setTypeDesc(Integer.class);
        pd.notifyTypeUpdated(PropertyDesc.PROBABILITY_MAXIMUM);
        pd.setMode(PropertyDesc.WRITE);
        pd.setAnnotationDesc(new MetaAnnotationDescImpl("b", new String[0],
                new Class[0]));
        pd.setAnnotationDesc(new AnnotationDescImpl("name2"));
        pd.setAnnotationDescOnGetter(new MetaAnnotationDescImpl("b_getter",
                new String[0], new Class[0]));
        pd.setAnnotationDescOnGetter(new AnnotationDescImpl("name1_getter",
                "body2"));
        pd.setAnnotationDescOnGetter(new AnnotationDescImpl("name2_getter"));
        pd.setAnnotationDescOnSetter(new MetaAnnotationDescImpl("b_setter",
                new String[0], new Class[0]));
        pd.setAnnotationDescOnSetter(new AnnotationDescImpl("name2_setter"));
        cd2.setPropertyDesc(pd);

        actual.merge(cd2, false);

        assertTrue("Meta系アノテーションもマージされる", actual.getPropertyDesc("param1")
                .hasMeta("a"));
        assertTrue("Meta系アノテーションもマージされる", actual.getPropertyDesc("param1")
                .hasMeta("b"));
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDesc("name1"));
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDesc("name2"));

        assertTrue("Meta系アノテーションもマージされること", actual.getPropertyDesc("param1")
                .hasMetaOnGetter("a_getter"));
        assertTrue("Meta系アノテーションもマージされること", actual.getPropertyDesc("param1")
                .hasMetaOnGetter("b_getter"));
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDescOnGetter("name1_getter"));
        assertEquals("Meta系でないアノテーションは同じアノテーションの場合forceに従って片方が優先されること",
                "body1", actual.getPropertyDesc("param1")
                        .getAnnotationDescOnGetter("name1_getter").getBody());
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDescOnGetter("name2_getter"));

        assertTrue("Meta系アノテーションもマージされること", actual.getPropertyDesc("param1")
                .hasMetaOnSetter("a_setter"));
        assertTrue("Meta系アノテーションもマージされること", actual.getPropertyDesc("param1")
                .hasMetaOnSetter("b_setter"));
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDescOnSetter("name1_setter"));
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDescOnSetter("name2_setter"));
    }

    public void testMerge4_YMIR_221_プロパティのアノテーションがマージされること() throws Exception {
        ClassDesc actual = new ClassDescImpl(pool_, "com.example.page.TestPage");
        PropertyDesc pd = new PropertyDescImpl(pool_, "param1");
        pd.setMode(PropertyDesc.READ);
        pd.setAnnotationDesc(new MetaAnnotationDescImpl("a", new String[0],
                new Class[0]));
        pd.setAnnotationDesc(new AnnotationDescImpl("name1"));
        pd.setAnnotationDescOnGetter(new MetaAnnotationDescImpl("a_getter",
                new String[0], new Class[0]));
        pd.setAnnotationDescOnGetter(new AnnotationDescImpl("name1_getter"));
        pd.setAnnotationDescOnSetter(new MetaAnnotationDescImpl("a_setter",
                new String[0], new Class[0]));
        pd.setAnnotationDescOnSetter(new AnnotationDescImpl("name1_setter"));
        actual.setPropertyDesc(pd);

        ClassDesc cd2 = new ClassDescImpl(pool_, "com.example.page.TestPage");
        cd2.setSuperclassName(TestPageBase.class.getName());
        pd = new PropertyDescImpl(pool_, "param1");
        pd.setTypeDesc(Integer.class);
        pd.notifyTypeUpdated(PropertyDesc.PROBABILITY_MAXIMUM);
        pd.setMode(PropertyDesc.WRITE);
        pd.setAnnotationDesc(new MetaAnnotationDescImpl("b", new String[0],
                new Class[0]));
        pd.setAnnotationDesc(new AnnotationDescImpl("name2"));
        pd.setAnnotationDescOnGetter(new MetaAnnotationDescImpl("b_getter",
                new String[0], new Class[0]));
        pd.setAnnotationDescOnGetter(new AnnotationDescImpl("name2_getter"));
        pd.setAnnotationDescOnSetter(new MetaAnnotationDescImpl("b_setter",
                new String[0], new Class[0]));
        pd.setAnnotationDescOnSetter(new AnnotationDescImpl("name2_setter"));
        cd2.setPropertyDesc(pd);

        actual.merge(cd2, true);

        assertTrue("Meta系アノテーションもマージされること", actual.getPropertyDesc("param1")
                .hasMeta("a"));
        assertTrue("Meta系アノテーションもマージされること", actual.getPropertyDesc("param1")
                .hasMeta("b"));
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDesc("name1"));
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDesc("name2"));

        assertTrue("Meta系アノテーションもマージされること", actual.getPropertyDesc("param1")
                .hasMetaOnGetter("a_getter"));
        assertTrue("Meta系アノテーションもマージされること", actual.getPropertyDesc("param1")
                .hasMetaOnGetter("b_getter"));
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDescOnGetter("name1_getter"));
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDescOnGetter("name2_getter"));

        assertTrue("Meta系アノテーションもマージされること", actual.getPropertyDesc("param1")
                .hasMetaOnSetter("a_setter"));
        assertTrue("Meta系アノテーションもマージされること", actual.getPropertyDesc("param1")
                .hasMetaOnSetter("b_setter"));
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDescOnSetter("name1_setter"));
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDescOnSetter("name2_setter"));
    }

    public void test_addDependingClassNamesTo() throws Exception {
        ClassDescImpl target = new ClassDescImpl(pool_, "name");
        target.setSuperclassName("org.example.Foe");
        target.setInterfaceTypeDescs(new TypeDescImpl(pool_, Map.class));
        target.setAnnotationDesc(new AnnotationDescImpl("org.example.Noe",
                "value"));
        MethodDescImpl methodDesc = new MethodDescImpl(pool_, "name");
        methodDesc.setReturnTypeDesc(new TypeDescImpl(pool_, "java.util.List<"
                + MethodDescImplTest.class.getName() + ">"));
        target.setMethodDesc(methodDesc);
        PropertyDescImpl propertyDesc = new PropertyDescImpl(pool_, "name");
        propertyDesc.setTypeDesc(new TypeDescImpl(pool_, "java.util.List<"
                + PropertyDescImplTest.class.getName() + ">"));
        target.setPropertyDesc(propertyDesc);

        TreeSet<String> set = new TreeSet<String>();

        target.addDependingClassNamesTo(set);

        String[] actual = set.toArray(new String[0]);
        assertEquals(6, actual.length);
        int idx = 0;
        assertEquals(List.class.getName(), actual[idx++]);
        assertEquals(Map.class.getName(), actual[idx++]);
        assertEquals("org.example.Foe", actual[idx++]);
        assertEquals("org.example.Noe", actual[idx++]);
        assertEquals(MethodDescImplTest.class.getName(), actual[idx++]);
        assertEquals(PropertyDescImplTest.class.getName(), actual[idx++]);
    }
}
