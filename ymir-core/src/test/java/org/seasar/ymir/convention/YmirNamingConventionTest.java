package org.seasar.ymir.convention;

import junit.framework.TestCase;

public class YmirNamingConventionTest extends TestCase {
    private YmirNamingConvention target_ = new YmirNamingConvention();

    public void testAddIgnorePackageName() throws Exception {
        target_.addRootPackageName("com.example");

        target_.addIgnorePackageName(".ignore1, com.example2.ignore");

        String[] actual = target_.getIgnorePackageNames();
        assertEquals(2, actual.length);
        int idx = 0;
        assertEquals("com.example.ignore1", actual[idx++]);
        assertEquals("com.example2.ignore", actual[idx++]);
    }
}
