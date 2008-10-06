package org.seasar.ymir.scope.impl;

import org.seasar.ymir.ComponentClientTestCase;
import org.seasar.ymir.annotation.MapParameter;
import org.seasar.ymir.impl.Bean;
import org.seasar.ymir.impl.PageComponentImpl;

public class ScopeManagerImplTest extends ComponentClientTestCase {
    private ScopeManagerImpl target_;

    private MapScope mapScope_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mapScope_ = new MapScope();
        register(mapScope_);

        target_ = getComponent(ScopeManagerImpl.class);
    }

    public void testPopulateScopeAttributes() throws Exception {
        mapScope_.setAttribute("bean1.aaa[1].bbb(key).mapped(key)", "value1");
        mapScope_.setAttribute("bean2.aaa[1].bbb(key).mapped(key)", "value2");
        mapScope_.setAttribute("bean3.aaa[1].bbb(key).mapped(key)", "value3");
        Page page = new Page();

        target_.populateScopeAttributes(
                new PageComponentImpl(page, Page.class), "_get");

        assertNull(page.getBean1().getAaa(1).getBbb("key").getMapped("key"));
        assertNull(page.getBean2().getAaa(1).getBbb("key").getMapped("key"));
        assertEquals("value3", page.getBean3().getAaa(1).getBbb("key")
                .getMapped("key"));

        page = new Page();

        target_.populateScopeAttributes(
                new PageComponentImpl(page, Page.class), "_post");

        assertNull(page.getBean1().getAaa(1).getBbb("key").getMapped("key"));
        assertEquals("value2", page.getBean2().getAaa(1).getBbb("key")
                .getMapped("key"));
        assertEquals("value3", page.getBean3().getAaa(1).getBbb("key")
                .getMapped("key"));
    }

    public static class Page {
        private Bean bean1_ = new Bean();

        private Bean bean2_ = new Bean();

        private Bean bean3_ = new Bean();

        public Bean getBean1() {
            return bean1_;
        }

        @MapParameter(actionName = "_post")
        public Bean getBean2() {
            return bean2_;
        }

        @MapParameter
        public Bean getBean3() {
            return bean3_;
        }
    }
}
