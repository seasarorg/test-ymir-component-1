package org.seasar.ymir.hotdeploy.impl;

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.ymir.testing.PageTestCase;

public class HotdeployManagerImplITest extends PageTestCase<Object> {
    @Override
    protected Class<Object> getPageClass() {
        return Object.class;
    }

    public void test_コンポーネントが正しく登録されていること() throws Exception {
        HotdeployManagerImpl actual = null;
        try {
            actual = getComponent(HotdeployManagerImpl.class);
        } catch (ComponentNotFoundRuntimeException ex) {
            fail();
        }

        assertEquals(5, actual.getHotdeployFitterBag().getFitters().length);
    }
}
