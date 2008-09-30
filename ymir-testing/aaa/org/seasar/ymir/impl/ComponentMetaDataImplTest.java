package org.seasar.ymir.impl;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.ymir.annotation.handler.impl.AnnotationHandlerImpl;
import org.seasar.ymir.cache.impl.CacheManagerImpl;
import org.seasar.ymir.hotdeploy.impl.HotdeployManagerImpl;
import org.seasar.ymir.scope.impl.RequestParameterScope;
import org.seasar.ymir.scope.impl.ScopeManagerImpl;
import org.seasar.ymir.scope.impl.SessionScope;

public class ComponentMetaDataImplTest extends TestCase {
    private ComponentMetaDataImpl target_;

    private AnnotationHandlerImpl annotationHandler_;

    HotdeployManagerImpl hotdeployManager_;

    private ScopeManagerImpl scopeManager_;

    private YmirTypeConversionManager typeConversionManager_;

    @Override
    protected void setUp() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.register(RequestParameterScope.class);
        container.register(SessionScope.class);
        annotationHandler_ = new AnnotationHandlerImpl();
        typeConversionManager_ = new YmirTypeConversionManager();
        hotdeployManager_ = new HotdeployManagerImpl();
        scopeManager_ = new ScopeManagerImpl();
        scopeManager_.setHotdeployManager(hotdeployManager_);
        scopeManager_.setTypeConversionManager(typeConversionManager_);
        CacheManagerImpl cacheManager = new CacheManagerImpl();
        cacheManager.setHotdeployManager(hotdeployManager_);
        annotationHandler_.setCacheManager(cacheManager);
        target_ = new ComponentMetaDataImpl(Hoe2Page.class, container,
                annotationHandler_, scopeManager_, typeConversionManager_) {
        };
    }

    public void testNonStrictInjectionの時にInアノテーションが付与されたメソッドがプロテクトされること()
            throws Exception {
        assertTrue(target_.isProtected("hoehoe"));
    }

    public void testNonStrictInjectionの時にOutアノテーションが付与されたメソッドがプロテクトされないこと()
            throws Exception {
        assertFalse(target_.isProtected("hoehoe2"));
    }

    public void testNonStrictInjectionの時にBindingアノテーションが付与されたメソッドがプロテクトされること()
            throws Exception {
        assertTrue(target_.isProtected("hoehoe3"));
    }

    public void testNonStrictInjectionの時にProtectedアノテーションが付与されたメソッドがプロテクトされること()
            throws Exception {
        assertTrue(target_.isProtected("hoehoe4"));
    }

    public void testNonStrictInjectionの時にFormFileのSetterメソッドがプロテクトされないこと()
            throws Exception {
        assertFalse(target_.isProtected("hoehoe5"));
    }

    public void testNonStrictInjectionの時にFormFileの配列のSetterメソッドがプロテクトされないこと()
            throws Exception {
        assertFalse(target_.isProtected("hoehoe6"));
    }

    public void testNonStrictInjectionの時にインタフェースを引数とするSetterメソッドがプロテクトされること()
            throws Exception {
        assertTrue(target_.isProtected("hoehoe7"));
    }

    public void testToAttributeName() throws Exception {
        assertEquals("a", target_.toAttributeName("getA", null));

        assertEquals("property", target_.toAttributeName("getProperty", null));

        assertEquals("URL", target_.toAttributeName("getURL", null));

        assertEquals("URLString", target_.toAttributeName("getURLString", null));

        assertEquals("hoe", target_.toAttributeName("setAttributeName", "hoe"));
    }

    public void testIsProtected() throws Exception {
        ComponentMetaDataImpl target = new ComponentMetaDataImpl(HoePage.class,
                S2ContainerFactory.create(getClass().getName()
                        .replace('.', '/').concat(".dicon")),
                annotationHandler_, scopeManager_, typeConversionManager_);

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
