package org.seasar.ymir.scope.handler.impl;

import org.seasar.ymir.ActionManager;
import org.seasar.ymir.ComponentClientTestCase;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.impl.Bean;
import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.scope.impl.MapScope;

public class ScopeAttributePopulatorTest extends ComponentClientTestCase {
    private ScopeAttributePopulatorImpl target_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        MapScope scope = new MapScope();
        target_ = new ScopeAttributePopulatorImpl(scope,
                getComponent(ActionManager.class),
                getComponent(AnnotationHandler.class),
                getComponent(ScopeManager.class),
                getComponent(TypeConversionManager.class));

        scope.setAttribute("bean.aaa[1].bbb(key).mapped(key)", "value");
        target_.addEntry(Page.class.getMethod("getBean", new Class[0]), false,
                new String[] { "_post" });
    }

    public void testPopulateTo() throws Exception {
        Page page = new Page();
        target_.populateTo(page, "_get");

        assertNull(page.getBean().getAaa(1).getBbb("key").getMapped("key"));

        target_.populateTo(page, "_post");

        assertEquals("value", page.getBean().getAaa(1).getBbb("key").getMapped(
                "key"));
    }

    public static class Page {
        private Bean bean_ = new Bean();

        public Bean getBean() {
            return bean_;
        }
    }
}
