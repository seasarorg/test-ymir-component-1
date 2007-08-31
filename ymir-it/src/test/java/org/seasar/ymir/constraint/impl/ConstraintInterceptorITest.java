package org.seasar.ymir.constraint.impl;

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.ymir.PageTestCase;

public class ConstraintInterceptorITest extends PageTestCase<Object> {
    @Override
    protected Class<Object> getPageClass() {
        return Object.class;
    }

    public void test_app_diconに登録したConstraintBundleコンポーネントが正しく登録されること()
            throws Exception {
        ConstraintInterceptor actual = null;
        try {
            actual = getComponent(ConstraintInterceptor.class);
        } catch (ComponentNotFoundRuntimeException ex) {
            fail();
        }

        assertEquals(1, actual.getConstraintBagsFromConstraintBundles().length);
    }
}
