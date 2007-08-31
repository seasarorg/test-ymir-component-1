package org.seasar.ymir.impl;

import junit.framework.TestCase;

public class DeniedPathMappingImplTest extends TestCase {
    public void test_isDeniedメソッドがtrueを返すこと() throws Exception {
        DeniedPathMappingImpl target = new DeniedPathMappingImpl("pattern");

        assertTrue(target.isDenied());
    }
}
