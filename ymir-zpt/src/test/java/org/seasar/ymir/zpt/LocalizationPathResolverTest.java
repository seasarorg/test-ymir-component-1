package org.seasar.ymir.zpt;

import junit.framework.TestCase;

public class LocalizationPathResolverTest extends TestCase {
    private LocalizationPathResolver target_ = new LocalizationPathResolver();

    public void testCreatePageSpecificKey() throws Exception {
        assertEquals("page.key", target_.createPageSpecificKey("key", "page"));

        assertEquals("label.page.key", target_.createPageSpecificKey(
                "label.key", "page"));

        assertEquals("key", target_.createPageSpecificKey("key", null));
    }
}
