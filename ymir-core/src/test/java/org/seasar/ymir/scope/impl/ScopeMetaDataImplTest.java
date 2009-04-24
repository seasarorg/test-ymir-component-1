package org.seasar.ymir.scope.impl;

import org.seasar.ymir.ActionManager;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.ComponentClientTestCase;
import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.scope.ScopeManager;

public class ScopeMetaDataImplTest extends ComponentClientTestCase {
    private ScopeMetaDataImpl target_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        register(RequestParameterScope.class);
        register(SessionScope.class);

        target_ = new ScopeMetaDataImpl(Hoe2Page.class, getContainer(),
                getComponent(ActionManager.class),
                getComponent(AnnotationHandler.class),
                getComponent(ApplicationManager.class),
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

    public void getHoe() {
    }

    public void testRegisterForOutjectionToScope() throws Exception {
        try {
            target_.registerForOutjectionToScope(null, getClass().getMethod(
                    "getHoe"));
            fail();
        } catch (IllegalClientCodeRuntimeException expected) {
        }
    }
}
