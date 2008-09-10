package org.seasar.ymir.scope.handler.impl;

import junit.framework.TestCase;

import org.seasar.ymir.YmirContext;
import org.seasar.ymir.hotdeploy.impl.HotdeployManagerImpl;
import org.seasar.ymir.impl.Bean;
import org.seasar.ymir.impl.YmirImpl;
import org.seasar.ymir.impl.YmirTypeConversionManager;
import org.seasar.ymir.scope.impl.MapScope;
import org.seasar.ymir.scope.impl.ScopeManagerImpl;

public class ScopeAttributePopulatorTest extends TestCase {
    private ScopeAttributePopulatorImpl target_;

    @Override
    protected void setUp() throws Exception {
        YmirContext.setYmir(new YmirImpl() {
            @Override
            public boolean isUnderDevelopment() {
                return false;
            }
        });

        MapScope scope = new MapScope();
        YmirTypeConversionManager typeConversionManager = new YmirTypeConversionManager();
        ScopeManagerImpl scopeManager = new ScopeManagerImpl();
        scopeManager.setHotdeployManager(new HotdeployManagerImpl());
        scopeManager.setTypeConversionManager(typeConversionManager);
        target_ = new ScopeAttributePopulatorImpl(scope, scopeManager,
                typeConversionManager);

        scope.setAttribute("bean.aaa[1].bbb(key).mapped(key)", "value");
        target_.addEntry(Page.class.getMethod("getBean", new Class[0]),
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
