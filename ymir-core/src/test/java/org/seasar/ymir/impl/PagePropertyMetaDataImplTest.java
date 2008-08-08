package org.seasar.ymir.impl;

import junit.framework.TestCase;

import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.ymir.annotation.handler.impl.AnnotationHandlerImpl;
import org.seasar.ymir.hotdeploy.impl.HotdeployManagerImpl;

public class PagePropertyMetaDataImplTest extends TestCase {
    private PageMetaDataImpl target_ = new PageMetaDataImpl(Object.class, null,
            new AnnotationHandlerImpl(), new HotdeployManagerImpl(),
            new YmirTypeConversionManager(), false);

    public void testToAttributeName() throws Exception {
        assertEquals("a", target_.toAttributeName("getA", null));

        assertEquals("property", target_.toAttributeName("getProperty", null));

        assertEquals("URL", target_.toAttributeName("getURL", null));

        assertEquals("URLString", target_.toAttributeName("getURLString", null));

        assertEquals("hoe", target_.toAttributeName("setAttributeName", "hoe"));
    }

    public void testIsProtected() throws Exception {
        PageMetaDataImpl target = new PageMetaDataImpl(HoePage.class,
                S2ContainerFactory.create(getClass().getName()
                        .replace('.', '/').concat(".dicon")),
                new AnnotationHandlerImpl(), new HotdeployManagerImpl(),
                new YmirTypeConversionManager(), false);

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
