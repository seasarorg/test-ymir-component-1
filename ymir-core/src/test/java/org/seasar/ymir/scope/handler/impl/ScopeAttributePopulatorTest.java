package org.seasar.ymir.scope.handler.impl;

import junit.framework.TestCase;

import org.seasar.ymir.YmirContext;
import org.seasar.ymir.hotdeploy.impl.HotdeployManagerImpl;
import org.seasar.ymir.impl.Bean;
import org.seasar.ymir.impl.YmirImpl;
import org.seasar.ymir.impl.YmirTypeConversionManager;
import org.seasar.ymir.scope.impl.MapScope;

public class ScopeAttributePopulatorTest extends TestCase {
    private MapScope scope_ = new MapScope();

    private ScopeAttributePopulator target_ = new ScopeAttributePopulator(
            scope_, new HotdeployManagerImpl(), new YmirTypeConversionManager());

    @Override
    protected void setUp() throws Exception {
        YmirContext.setYmir(new YmirImpl() {
            @Override
            public boolean isUnderDevelopment() {
                return false;
            }
        });
        scope_.setAttribute("bean.aaa[1].bbb(key).mapped(key)", "value");
        target_.addEntry(Page.class.getMethod("getBean", new Class[0]),
                new String[] { "_post" });
    }

    public void testInjectTo() throws Exception {
        Page page = new Page();
        target_.injectTo(page, "_get");

        assertNull(page.getBean().getAaa(1).getBbb("key").getMapped("key"));

        target_.injectTo(page, "_post");

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
