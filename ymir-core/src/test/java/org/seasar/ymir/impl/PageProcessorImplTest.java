package org.seasar.ymir.impl;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.annotation.MapParameter;
import org.seasar.ymir.annotation.handler.impl.AnnotationHandlerImpl;
import org.seasar.ymir.hotdeploy.impl.HotdeployManagerImpl;
import org.seasar.ymir.scope.impl.MapScope;

public class PageProcessorImplTest extends TestCase {
    private PageProcessorImpl target_;

    private ComponentMetaDataImpl metaData_;

    private MapScope mapScope_;

    @Override
    protected void setUp() throws Exception {
        YmirContext.setYmir(new YmirImpl() {
            public boolean isUnderDevelopment() {
                return false;
            }
        });
        target_ = new PageProcessorImpl();
        S2Container container = new S2ContainerImpl();
        container.register(MapScope.class);
        mapScope_ = (MapScope) container.getComponent(MapScope.class);
        metaData_ = new ComponentMetaDataImpl(Page.class, container,
                new AnnotationHandlerImpl(), new HotdeployManagerImpl(),
                new YmirTypeConversionManager());
    }

    public void testPopulateScopeAttributes() throws Exception {
        mapScope_.setAttribute("bean1.aaa[1].bbb(key).mapped(key)", "value1");
        mapScope_.setAttribute("bean2.aaa[1].bbb(key).mapped(key)", "value2");
        mapScope_.setAttribute("bean3.aaa[1].bbb(key).mapped(key)", "value3");
        Page page = new Page();

        target_.populateScopeAttributes(page, metaData_, "_get");

        assertNull(page.getBean1().getAaa(1).getBbb("key").getMapped("key"));
        assertNull(page.getBean2().getAaa(1).getBbb("key").getMapped("key"));
        assertEquals("value3", page.getBean3().getAaa(1).getBbb("key")
                .getMapped("key"));

        page = new Page();

        target_.populateScopeAttributes(page, metaData_, "_post");

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
