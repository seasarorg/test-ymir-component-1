package org.seasar.ymir.converter.impl;

import junit.framework.TestCase;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.seasar.ymir.converter.impl.BeanUtilsPropertyHandler;
import org.seasar.ymir.impl.Bean;
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
}
