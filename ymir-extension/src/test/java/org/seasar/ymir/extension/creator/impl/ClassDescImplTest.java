package org.seasar.ymir.extension.creator.impl;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;

import com.example.page.TestPageBase;

public class ClassDescImplTest extends SourceCreatorImplTestBase {
    public void testGetInstanceName() throws Exception {
        String actual = new ClassDescImpl("com.example.dto.TestDto")
                .getInstanceName();
        assertEquals("testDto", actual);
    }

    public void testMerge() throws Exception {
        ClassDesc actual = new ClassDescImpl("com.example.page.TestPage");
        PropertyDesc pd = new PropertyDescImpl("param1");
        pd.setMode(PropertyDesc.READ);
        actual.setPropertyDesc(pd);
        pd = new PropertyDescImpl("param2");
        pd.setMode(PropertyDesc.WRITE);
        actual.setPropertyDesc(pd);
        pd = new PropertyDescImpl("param3");
        pd.setTypeDesc("java.lang.String");
        pd.getTypeDesc().setExplicit(true);
        actual.setPropertyDesc(pd);
        pd = new PropertyDescImpl("param4");
        pd.setTypeDesc("java.lang.String[]");
        actual.setPropertyDesc(pd);
        pd = new PropertyDescImpl("param6");
        pd.setTypeDesc("java.lang.Integer");
        actual.setPropertyDesc(pd);
        pd = new PropertyDescImpl("param7");
        pd.setTypeDesc("java.lang.String");
        actual.setPropertyDesc(pd);
        MethodDesc md = new MethodDescImpl("method");
        md.setReturnTypeDesc("java.lang.String");
        actual.setMethodDesc(md);
        md = new MethodDescImpl("method2");
        md.setReturnTypeDesc("java.lang.String", true);
        actual.setMethodDesc(md);
        md = new MethodDescImpl("method3");
        md.setReturnTypeDesc("java.lang.String", true);
        actual.setMethodDesc(md);
        md = new MethodDescImpl("method4");
        md.setReturnTypeDesc("java.lang.String");
        actual.setMethodDesc(md);
        md = new MethodDescImpl("method5");
        md.setReturnTypeDesc("java.lang.String");
        md.setAnnotationDesc(new MetaAnnotationDescImpl("a", new String[0],
                new Class[0]));
        md.setAnnotationDesc(new AnnotationDescImpl("name1"));
        actual.setMethodDesc(md);

        ClassDesc cd2 = new ClassDescImpl("com.example.page.TestPage");
        cd2.setSuperclassName(TestPageBase.class.getName());
        pd = new PropertyDescImpl("param1");
        pd.setTypeDesc("java.lang.Integer");
        pd.getTypeDesc().setExplicit(true);
        pd.setMode(PropertyDesc.WRITE);
        cd2.setPropertyDesc(pd);
        pd = new PropertyDescImpl("param2");
        pd.setTypeDesc("java.lang.Integer");
        pd.getTypeDesc().setExplicit(true);
        pd.setMode(PropertyDesc.READ);
        cd2.setPropertyDesc(pd);
        pd = new PropertyDescImpl("param3");
        pd.setTypeDesc("java.lang.Integer");
        pd.getTypeDesc().setExplicit(true);
        cd2.setPropertyDesc(pd);
        pd = new PropertyDescImpl("param5");
        pd.setTypeDesc("java.lang.Integer[]");
        pd.getTypeDesc().setExplicit(true);
        cd2.setPropertyDesc(pd);
        pd = new PropertyDescImpl("param7");
        pd.setTypeDesc("java.lang.Integer");
        cd2.setPropertyDesc(pd);
        md = new MethodDescImpl("method");
        md.setReturnTypeDesc("java.lang.Integer", true);
        md.setBodyDesc(new BodyDescImpl("body"));
        cd2.setMethodDesc(md);
        md = new MethodDescImpl("method2");
        md.setReturnTypeDesc("java.lang.Integer");
        md.setBodyDesc(new BodyDescImpl("body"));
        cd2.setMethodDesc(md);
        md = new MethodDescImpl("method3");
        md.setReturnTypeDesc("java.lang.Integer", true);
        md.setBodyDesc(new BodyDescImpl("body"));
        cd2.setMethodDesc(md);
        md = new MethodDescImpl("method4");
        md.setReturnTypeDesc("java.lang.Integer");
        md.setBodyDesc(new BodyDescImpl("body"));
        cd2.setMethodDesc(md);
        md = new MethodDescImpl("method5");
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
        MethodDesc actualMd = actual
                .getMethodDesc(new MethodDescImpl("method"));
        assertNotNull(actualMd);
        assertEquals("body", actualMd.getBodyDesc().getRoot().get("body"));
        assertEquals("Integer", actual.getMethodDesc(
                new MethodDescImpl("method")).getReturnTypeDesc().getName());
        assertEquals("String", actual.getMethodDesc(
                new MethodDescImpl("method2")).getReturnTypeDesc().getName());
        assertEquals("メソッドの返り値のtypeが両方ともexplicitである場合はもともとのtypeが優先されること",
                "String", actual.getMethodDesc(new MethodDescImpl("method3"))
                        .getReturnTypeDesc().getName());
        assertEquals("メソッドの返り値のtypeが両方ともimplicitである場合はもともとのtypeが優先されること",
                "String", actual.getMethodDesc(new MethodDescImpl("method4"))
                        .getReturnTypeDesc().getName());
        assertNotNull("メソッドのMeta系でないアノテーションはマージされること", actual.getMethodDesc(
                new MethodDescImpl("method5")).getAnnotationDesc("name1"));
        assertNotNull("メソッドのMeta系でないアノテーションはマージされること", actual.getMethodDesc(
                new MethodDescImpl("method5")).getAnnotationDesc("name2"));
        assertNotNull("メソッドのMeta系アノテーションはマージされないこと", actual.getMethodDesc(
                new MethodDescImpl("method5")).getMetaValues("a"));
        assertNull("メソッドのMeta系アノテーションはマージされないこと", actual.getMethodDesc(
                new MethodDescImpl("method5")).getMetaValues("b"));
    }

    public void testMerge2() throws Exception {
        ClassDesc actual = new ClassDescImpl("com.example.page.TestPage");
        PropertyDesc pd = new PropertyDescImpl("param1");
        pd.setMode(PropertyDesc.READ);
        actual.setPropertyDesc(pd);
        pd = new PropertyDescImpl("param2");
        pd.setMode(PropertyDesc.WRITE);
        actual.setPropertyDesc(pd);
        pd = new PropertyDescImpl("param3");
        pd.setTypeDesc("java.lang.String");
        pd.getTypeDesc().setExplicit(true);
        actual.setPropertyDesc(pd);
        pd = new PropertyDescImpl("param4");
        pd.setTypeDesc("java.lang.String[]");
        actual.setPropertyDesc(pd);
        pd = new PropertyDescImpl("param6");
        pd.setTypeDesc("java.lang.Integer");
        actual.setPropertyDesc(pd);
        pd = new PropertyDescImpl("param7");
        pd.setTypeDesc("java.lang.String");
        actual.setPropertyDesc(pd);
        MethodDesc md = new MethodDescImpl("method");
        md.setReturnTypeDesc("java.lang.String");
        actual.setMethodDesc(md);
        md = new MethodDescImpl("method2");
        md.setReturnTypeDesc("java.lang.String", true);
        actual.setMethodDesc(md);
        md = new MethodDescImpl("method3");
        md.setReturnTypeDesc("java.lang.String", true);
        actual.setMethodDesc(md);
        md = new MethodDescImpl("method4");
        md.setReturnTypeDesc("java.lang.String");
        actual.setMethodDesc(md);
        md = new MethodDescImpl("method5");
        md.setReturnTypeDesc("java.lang.String");
        md.setAnnotationDesc(new MetaAnnotationDescImpl("a", new String[0],
                new Class[0]));
        md.setAnnotationDesc(new AnnotationDescImpl("name1"));
        actual.setMethodDesc(md);

        ClassDesc cd2 = new ClassDescImpl("com.example.page.TestPage");
        cd2.setSuperclassName(TestPageBase.class.getName());
        pd = new PropertyDescImpl("param1");
        pd.setTypeDesc("java.lang.Integer");
        pd.getTypeDesc().setExplicit(true);
        pd.setMode(PropertyDesc.WRITE);
        cd2.setPropertyDesc(pd);
        pd = new PropertyDescImpl("param2");
        pd.setTypeDesc("java.lang.Integer");
        pd.getTypeDesc().setExplicit(true);
        pd.setMode(PropertyDesc.READ);
        cd2.setPropertyDesc(pd);
        pd = new PropertyDescImpl("param3");
        pd.setTypeDesc("java.lang.Integer");
        pd.getTypeDesc().setExplicit(true);
        cd2.setPropertyDesc(pd);
        pd = new PropertyDescImpl("param5");
        pd.setTypeDesc("java.lang.Integer[]");
        pd.getTypeDesc().setExplicit(true);
        cd2.setPropertyDesc(pd);
        pd = new PropertyDescImpl("param7");
        pd.setTypeDesc("java.lang.Integer");
        cd2.setPropertyDesc(pd);
        md = new MethodDescImpl("method");
        md.setReturnTypeDesc("java.lang.Integer", true);
        md.setBodyDesc(new BodyDescImpl("body"));
        cd2.setMethodDesc(md);
        md = new MethodDescImpl("method2");
        md.setReturnTypeDesc("java.lang.Integer");
        md.setBodyDesc(new BodyDescImpl("body"));
        cd2.setMethodDesc(md);
        md = new MethodDescImpl("method3");
        md.setReturnTypeDesc("java.lang.Integer", true);
        md.setBodyDesc(new BodyDescImpl("body"));
        cd2.setMethodDesc(md);
        md = new MethodDescImpl("method4");
        md.setReturnTypeDesc("java.lang.Integer");
        md.setBodyDesc(new BodyDescImpl("body"));
        cd2.setMethodDesc(md);
        md = new MethodDescImpl("method5");
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
        MethodDesc actualMd = actual
                .getMethodDesc(new MethodDescImpl("method"));
        assertNotNull(actualMd);
        assertEquals("body", actualMd.getBodyDesc().getRoot().get("body"));
        assertEquals("Integer", actual.getMethodDesc(
                new MethodDescImpl("method")).getReturnTypeDesc().getName());
        assertEquals("String", actual.getMethodDesc(
                new MethodDescImpl("method2")).getReturnTypeDesc().getName());
        assertEquals("メソッドの返り値のtypeが両方ともexplicitである場合は引数で指定された側のtypeが優先されること",
                "Integer", actual.getMethodDesc(new MethodDescImpl("method3"))
                        .getReturnTypeDesc().getName());
        assertEquals("メソッドの返り値のtypeが両方ともimplicitである場合は引数で指定された側のtypeが優先されること",
                "Integer", actual.getMethodDesc(new MethodDescImpl("method4"))
                        .getReturnTypeDesc().getName());
        assertNotNull("メソッドのMeta系でないアノテーションはマージされること", actual.getMethodDesc(
                new MethodDescImpl("method5")).getAnnotationDesc("name1"));
        assertNotNull("メソッドのMeta系でないアノテーションはマージされること", actual.getMethodDesc(
                new MethodDescImpl("method5")).getAnnotationDesc("name2"));
        assertNull("メソッドのMeta系アノテーションはマージされないこと", actual.getMethodDesc(
                new MethodDescImpl("method5")).getMetaValues("a"));
        assertNotNull("メソッドのMeta系アノテーションはマージされないこと", actual.getMethodDesc(
                new MethodDescImpl("method5")).getMetaValues("b"));
    }

    public void testMerge3_YMIR_221_プロパティのアノテーションがマージされること() throws Exception {
        ClassDesc actual = new ClassDescImpl("com.example.page.TestPage");
        PropertyDesc pd = new PropertyDescImpl("param1");
        pd.setMode(PropertyDesc.READ);
        pd.setAnnotationDesc(new MetaAnnotationDescImpl("a", new String[0],
                new Class[0]));
        pd.setAnnotationDesc(new AnnotationDescImpl("name1"));
        pd.setAnnotationDescForGetter(new MetaAnnotationDescImpl("a_getter",
                new String[0], new Class[0]));
        pd.setAnnotationDescForGetter(new AnnotationDescImpl("name1_getter"));
        pd.setAnnotationDescForSetter(new MetaAnnotationDescImpl("a_setter",
                new String[0], new Class[0]));
        pd.setAnnotationDescForSetter(new AnnotationDescImpl("name1_setter"));
        actual.setPropertyDesc(pd);

        ClassDesc cd2 = new ClassDescImpl("com.example.page.TestPage");
        cd2.setSuperclassName(TestPageBase.class.getName());
        pd = new PropertyDescImpl("param1");
        pd.setTypeDesc("java.lang.Integer");
        pd.getTypeDesc().setExplicit(true);
        pd.setMode(PropertyDesc.WRITE);
        pd.setAnnotationDesc(new MetaAnnotationDescImpl("b", new String[0],
                new Class[0]));
        pd.setAnnotationDesc(new AnnotationDescImpl("name2"));
        pd.setAnnotationDescForGetter(new MetaAnnotationDescImpl("b_getter",
                new String[0], new Class[0]));
        pd.setAnnotationDescForGetter(new AnnotationDescImpl("name2_getter"));
        pd.setAnnotationDescForSetter(new MetaAnnotationDescImpl("b_setter",
                new String[0], new Class[0]));
        pd.setAnnotationDescForSetter(new AnnotationDescImpl("name2_setter"));
        cd2.setPropertyDesc(pd);

        actual.merge(cd2, false);

        assertEquals("Meta系アノテーションはマージされないこと", "a",
                ((MetaAnnotationDescImpl) actual.getPropertyDesc("param1")
                        .getAnnotationDesc(Meta.class.getName())).getMetaName());
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDesc("name1"));
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDesc("name2"));

        assertEquals("Meta系アノテーションはマージされないこと", "a_getter",
                ((MetaAnnotationDescImpl) actual.getPropertyDesc("param1")
                        .getAnnotationDescForGetter(Meta.class.getName()))
                        .getMetaName());
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDescForGetter("name1_getter"));
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDescForGetter("name2_getter"));

        assertEquals("Meta系アノテーションはマージされないこと", "a_setter",
                ((MetaAnnotationDescImpl) actual.getPropertyDesc("param1")
                        .getAnnotationDescForSetter(Meta.class.getName()))
                        .getMetaName());
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDescForSetter("name1_setter"));
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDescForSetter("name2_setter"));
    }

    public void testMerge4_YMIR_221_プロパティのアノテーションがマージされること() throws Exception {
        ClassDesc actual = new ClassDescImpl("com.example.page.TestPage");
        PropertyDesc pd = new PropertyDescImpl("param1");
        pd.setMode(PropertyDesc.READ);
        pd.setAnnotationDesc(new MetaAnnotationDescImpl("a", new String[0],
                new Class[0]));
        pd.setAnnotationDesc(new AnnotationDescImpl("name1"));
        pd.setAnnotationDescForGetter(new MetaAnnotationDescImpl("a_getter",
                new String[0], new Class[0]));
        pd.setAnnotationDescForGetter(new AnnotationDescImpl("name1_getter"));
        pd.setAnnotationDescForSetter(new MetaAnnotationDescImpl("a_setter",
                new String[0], new Class[0]));
        pd.setAnnotationDescForSetter(new AnnotationDescImpl("name1_setter"));
        actual.setPropertyDesc(pd);

        ClassDesc cd2 = new ClassDescImpl("com.example.page.TestPage");
        cd2.setSuperclassName(TestPageBase.class.getName());
        pd = new PropertyDescImpl("param1");
        pd.setTypeDesc("java.lang.Integer");
        pd.getTypeDesc().setExplicit(true);
        pd.setMode(PropertyDesc.WRITE);
        pd.setAnnotationDesc(new MetaAnnotationDescImpl("b", new String[0],
                new Class[0]));
        pd.setAnnotationDesc(new AnnotationDescImpl("name2"));
        pd.setAnnotationDescForGetter(new MetaAnnotationDescImpl("b_getter",
                new String[0], new Class[0]));
        pd.setAnnotationDescForGetter(new AnnotationDescImpl("name2_getter"));
        pd.setAnnotationDescForSetter(new MetaAnnotationDescImpl("b_setter",
                new String[0], new Class[0]));
        pd.setAnnotationDescForSetter(new AnnotationDescImpl("name2_setter"));
        cd2.setPropertyDesc(pd);

        actual.merge(cd2, true);

        assertEquals("Meta系アノテーションはマージされないこと", "b",
                ((MetaAnnotationDescImpl) actual.getPropertyDesc("param1")
                        .getAnnotationDesc(Meta.class.getName())).getMetaName());
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDesc("name1"));
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDesc("name2"));

        assertEquals("Meta系アノテーションはマージされないこと", "b_getter",
                ((MetaAnnotationDescImpl) actual.getPropertyDesc("param1")
                        .getAnnotationDescForGetter(Meta.class.getName()))
                        .getMetaName());
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDescForGetter("name1_getter"));
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDescForGetter("name2_getter"));

        assertEquals("Meta系アノテーションはマージされないこと", "b_setter",
                ((MetaAnnotationDescImpl) actual.getPropertyDesc("param1")
                        .getAnnotationDescForSetter(Meta.class.getName()))
                        .getMetaName());
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDescForSetter("name1_setter"));
        assertNotNull("Meta系でないアノテーションはマージされること", actual.getPropertyDesc(
                "param1").getAnnotationDescForSetter("name2_setter"));
    }
}
