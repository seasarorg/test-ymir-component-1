package org.seasar.ymir.impl;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
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

    public void testToAttributeName() throws Exception {
        assertEquals("a", target_.toAttributeName("getA", null));

        assertEquals("property", target_.toAttributeName("getProperty", null));

        assertEquals("URL", target_.toAttributeName("getURL", null));

        assertEquals("URLString", target_.toAttributeName("getURLString", null));

        assertEquals("hoe", target_.toAttributeName("setAttributeName", "hoe"));
    }
}
