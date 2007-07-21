package org.seasar.ymir;

import junit.framework.TestCase;

import org.seasar.cms.pluggable.SingletonPluggableContainerFactory;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.customizer.AspectCustomizer;
import org.seasar.ymir.creator.PageCreator;

public class YmirDiconTest extends TestCase {
    public void test_Creator定義を差し替えられること() throws Exception {
        SingletonPluggableContainerFactory.prepareForContainer();
        SingletonPluggableContainerFactory.integrate(
                "org/seasar/ymir/ymir.dicon", new S2Container[0]);
        SingletonPluggableContainerFactory.init();
        S2Container container = SingletonPluggableContainerFactory
                .getRootContainer();

        PageCreator pageCreator = (PageCreator) container
                .getComponent("pageCreator");
        AspectCustomizer customizer = (AspectCustomizer) pageCreator
                .getPageCustomizer();
        assertNotNull(customizer);
    }
}
