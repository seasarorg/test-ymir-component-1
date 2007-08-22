package org.seasar.ymir.impl;

import junit.framework.TestCase;

public class PagePropertyMetaDataImplTest extends TestCase {
    private PagePropertyMetaDataImpl target_ = new PagePropertyMetaDataImpl(
            Object.class, null);

    public void testToAttributeName() throws Exception {
        assertEquals("a", target_.toAttributeName("getA", null));

        assertEquals("property", target_.toAttributeName("getProperty", null));

        assertEquals("URL", target_.toAttributeName("getURL", null));

        assertEquals("URLString", target_.toAttributeName("getURLString", null));

        assertEquals("hoe", target_.toAttributeName("setAttributeName", "hoe"));
    }

    public void testIsProtected() throws Exception {
        PagePropertyMetaDataImpl target = new PagePropertyMetaDataImpl(
                HoePage.class, null);

        assertTrue(target.isProtected("map"));
        assertTrue(target.isProtected("maps"));
        assertFalse(target.isProtected("string"));
        assertFalse(target.isProtected("strings"));
        assertTrue(target.isProtected("protected"));
        assertTrue(target.isProtected("in"));
        assertTrue(target.isProtected("component"));
        assertFalse("getterに@Outがついていてもsetterがプロテクトされないこと", target
                .isProtected("out"));
        assertFalse(target.isProtected("file"));
        assertFalse(target.isProtected("files"));
    }
}