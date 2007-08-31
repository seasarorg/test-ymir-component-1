package org.seasar.ymir.util;

import junit.framework.TestCase;

public class MessagesUtilsTest extends TestCase {
    public void testGetPageSpecificName() throws Exception {
        assertEquals("page.key", MessagesUtils.getPageSpecificName("key",
                "page"));

        assertEquals("label.page.key", MessagesUtils.getPageSpecificName(
                "label.key", "page"));

        assertNull(MessagesUtils.getPageSpecificName("key", null));
    }
}
