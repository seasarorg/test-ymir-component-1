package org.seasar.ymir.hotdeploy;

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.ymir.PageTestCase;
import org.seasar.ymir.hotdeploy.impl.HotdeployManagerImpl;

public class HotdeployManagerITest extends PageTestCase<Object> {
    @Override
    protected Class<Object> getPageClass() {
        return Object.class;
    }

    public void test() throws Exception {
        HotdeployManagerImpl actual = null;
        try {
            actual = getComponent(HotdeployManagerImpl.class);
        } catch (ComponentNotFoundRuntimeException ex) {
            fail();
        }

        assertEquals(2, actual.getHotdeployFitters().length);
    }
}
