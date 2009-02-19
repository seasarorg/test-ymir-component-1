package org.seasar.ymir.converter.impl;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.seasar.ymir.impl.Bean;
import org.seasar.ymir.impl.Bean.Aaa;
import org.seasar.ymir.impl.Bean.Bbb;

public class BeanUtilsPropertyHandlerTest extends TestCase {
    private Bean target1Bean_ = new Bean();

    private BeanUtilsPropertyHandler target1_;

    private Bean target2Bean_ = new Bean();

    private BeanUtilsPropertyHandler target2_;

    private Bean target3Bean_ = new Bean();

    private BeanUtilsPropertyHandler target3_;

    @Override
    protected void setUp() throws Exception {
        PropertyUtilsBean pub = new PropertyUtilsBean();
        target1_ = new BeanUtilsPropertyHandler(pub, pub.getPropertyDescriptor(
                target1Bean_, "aaa[1].bbb(key).indexed[1]"), target1Bean_,
                "aaa[1].bbb(key).indexed[1]");
        target2_ = new BeanUtilsPropertyHandler(pub, pub.getPropertyDescriptor(
                target2Bean_, "aaa[1].bbb(key).mapped(key)"), target2Bean_,
                "aaa[1].bbb(key).mapped(key)");
        target3_ = new BeanUtilsPropertyHandler(pub, pub.getPropertyDescriptor(
                target3Bean_, "aaa[1].bbb(key).simple"), target3Bean_,
                "aaa[1].bbb(key).simple");
    }

    public void testGetPropertyType() throws Exception {
        assertEquals(String.class, target1_.getPropertyType());
        assertEquals(String.class, target2_.getPropertyType());
        assertEquals(String.class, target3_.getPropertyType());
    }

    public void testGetReadMethod() throws Exception {
        assertEquals(Bbb.class.getMethod("getIndexed",
                new Class[] { Integer.TYPE }), target1_.getReadMethod());
        assertEquals(Bbb.class.getMethod("getMapped",
                new Class[] { String.class }), target2_.getReadMethod());
        assertEquals(Bbb.class.getMethod("getSimple", new Class[0]), target3_
                .getReadMethod());
    }

    public void testGetWriteMethod() throws Exception {
        assertEquals(Bbb.class.getMethod("setIndexed", new Class[] {
            Integer.TYPE, String.class }), target1_.getWriteMethod());
        assertEquals(Bbb.class.getMethod("setMapped", new Class[] {
            String.class, String.class }), target2_.getWriteMethod());
        assertEquals(Bbb.class.getMethod("setSimple",
                new Class[] { String.class }), target3_.getWriteMethod());
    }

    public void testSetProperty() throws Exception {
        target1_.setProperty("value1");
        assertEquals("value1", target1Bean_.getAaa(1).getBbb("key").getIndexed(
                1));

        target2_.setProperty("value2");
        assertEquals("value2", target2Bean_.getAaa(1).getBbb("key").getMapped(
                "key"));

        target3_.setProperty("value3");
        assertEquals("value3", target3Bean_.getAaa(1).getBbb("key").getSimple());
    }

    public void testGetPropertyType2_プロパティキーが配列型のプロパティを指す場合は配列型を返すこと()
            throws Exception {
        PropertyUtilsBean pub = new PropertyUtilsBean();
        Bean bean = new Bean();
        BeanUtilsPropertyHandler target = new BeanUtilsPropertyHandler(pub, pub
                .getPropertyDescriptor(bean, "aaas"), bean, "aaas");

        assertEquals(Aaa[].class, target.getPropertyType());
    }

    public void testGetPropertyType3_プロパティキーが配列型のプロパティの要素を指す場合はコンポーネント型を返すこと()
            throws Exception {
        PropertyUtilsBean pub = new PropertyUtilsBean();
        Bean bean = new Bean();
        BeanUtilsPropertyHandler target = new BeanUtilsPropertyHandler(pub, pub
                .getPropertyDescriptor(bean, "aaas[1]"), bean, "aaas[1]");

        assertEquals(Aaa.class, target.getPropertyType());
    }

    public void testGetPropertyType4_プロパティキーがList型のプロパティを指す場合はList型を返すこと()
            throws Exception {
        PropertyUtilsBean pub = new PropertyUtilsBean();
        Bean bean = new Bean();
        BeanUtilsPropertyHandler target = new BeanUtilsPropertyHandler(pub, pub
                .getPropertyDescriptor(bean, "aaaList"), bean, "aaaList");

        assertEquals(List.class, target.getPropertyType());
    }

    public void testGetPropertyType5_プロパティキーがList型のプロパティの要素を指す場合はコンポーネント型を返すこと()
            throws Exception {
        PropertyUtilsBean pub = new PropertyUtilsBean();
        Bean bean = new Bean();
        BeanUtilsPropertyHandler target = new BeanUtilsPropertyHandler(pub, pub
                .getPropertyDescriptor(bean, "aaaList[1]"), bean, "aaaList[1]");

        assertEquals(Aaa.class, target.getPropertyType());
    }

    public void testToClass() throws Exception {
        assertNull(target1_.toClass(null));

        Method method = getClass().getMethod("method1", String.class,
                List.class);

        assertEquals(String.class, target1_.toClass(method
                .getGenericParameterTypes()[0]));

        assertEquals(List.class, target1_.toClass(method
                .getGenericParameterTypes()[1]));
    }

    public void method1(String arg0, List<String> arg1) {
    }
}
