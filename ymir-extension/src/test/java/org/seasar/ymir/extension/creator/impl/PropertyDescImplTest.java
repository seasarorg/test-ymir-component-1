package org.seasar.ymir.extension.creator.impl;

import junit.framework.TestCase;

public class PropertyDescImplTest extends TestCase {
    public void testGetGetterName() throws Exception {
        PropertyDescImpl target = new PropertyDescImpl("value");
        target.setTypeDesc("boolean");
        assertEquals("isValue", target.getGetterName());
    }

    public void testGetGetterName2() throws Exception {
        PropertyDescImpl target = new PropertyDescImpl("value");
        target.setTypeDesc("String");
        assertEquals("getValue", target.getGetterName());
    }

    public void testGetGetterName3() throws Exception {
        PropertyDescImpl target = new PropertyDescImpl("value");
        target.setTypeDesc("boolean");
        target.setGetterName("getValue");
        assertEquals("getValue", target.getGetterName());
    }
}
