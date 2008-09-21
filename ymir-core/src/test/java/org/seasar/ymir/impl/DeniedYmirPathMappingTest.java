package org.seasar.ymir.impl;

import junit.framework.TestCase;

public class DeniedYmirPathMappingTest extends TestCase {
    public void test_isDeniedメソッドがtrueを返すこと() throws Exception {
        DeniedYmirPathMapping target = new DeniedYmirPathMapping("pattern");

        assertTrue(target.isDenied());
    }
}
