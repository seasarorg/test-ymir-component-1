package org.seasar.ymir.util;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;

import junit.framework.TestCase;

public class ContainerUtilsTest extends TestCase {
    public void testFindAllAndAscendantComponentDefs() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.register(new ComponentDefImpl(Aaa1.class));
        S2Container child = new S2ContainerImpl();
        child.register(new ComponentDefImpl(Aaa2.class));
        S2Container parent = new S2ContainerImpl();
        parent.register(new ComponentDefImpl(Aaa3.class));
        parent.include(container);
        container.include(child);

        ComponentDef[] actual = ContainerUtils
                .findAllAndAscendantComponentDefs(container, IAaa.class);

        assertEquals(3, actual.length);
    }
}
