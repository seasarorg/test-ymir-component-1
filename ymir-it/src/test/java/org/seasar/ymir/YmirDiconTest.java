package org.seasar.ymir;

import junit.framework.TestCase;

import org.seasar.cms.pluggable.SingletonPluggableContainerFactory;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.customizer.AspectCustomizer;
import org.seasar.ymir.creator.PageCreator;

public class YmirDiconTest extends TestCase {
    private S2Container container_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        SingletonPluggableContainerFactory.prepareForContainer();
        SingletonPluggableContainerFactory.integrate(
                "org/seasar/ymir/ymir.dicon", new S2Container[0]);
        SingletonPluggableContainerFactory.init();
        container_ = SingletonPluggableContainerFactory.getRootContainer();
    }

    @Override
    protected void tearDown() throws Exception {
        SingletonPluggableContainerFactory.destroy();
        super.tearDown();
    }

    public void test_Creator定義を差し替えられること() throws Exception {
        PageCreator pageCreator = (PageCreator) container_
                .getComponent("pageCreator");
        AspectCustomizer customizer = (AspectCustomizer) pageCreator
                .getPageCustomizer();
        assertNotNull(customizer);
    }
}
