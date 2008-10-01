package org.seasar.ymir.impl;

import org.seasar.ymir.ComponentClientTestCase;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.scope.impl.RequestParameterScope;
import org.seasar.ymir.scope.impl.SessionScope;

public class ComponentMetaDataImplTest extends ComponentClientTestCase {
    private ComponentMetaDataImpl target_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        register(RequestParameterScope.class);
        register(SessionScope.class);

        target_ = new ComponentMetaDataImpl(Hoe2Page.class, getContainer(),
                getComponent(AnnotationHandler.class),
                getComponent(ScopeManager.class),
                getComponent(TypeConversionManager.class)) {
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
