package org.seasar.ymir.zpt;

import junit.framework.TestCase;

public class TalAttributesTest extends TestCase {
    public void test_toFilteredString() throws Exception {
        TalAttributes target = TalAttributes.valueOf("hoe a/b/c; fuga d/e/;;f");
        assertEquals("hoe a/b/c; fuga d/e/;;f", target.toFilteredString());
    }
}
