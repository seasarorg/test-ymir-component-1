package org.seasar.ymir.util;

import junit.framework.TestCase;

public class MessagesUtilsTest extends TestCase {
    public void test_getMessageNameCandidates() throws Exception {
        String[] actual = MessagesUtils.getMessageNameCandidates("key",
                new String[0]);
        assertEquals(1, actual.length);
        int idx = 0;
        assertEquals("key", actual[idx++]);

        actual = MessagesUtils.getMessageNameCandidates("label.key",
                new String[0]);
        assertEquals(1, actual.length);
        idx = 0;
        assertEquals("label.key", actual[idx++]);

        actual = MessagesUtils.getMessageNameCandidates("key", new String[] {
            "indexPage", "index", });
        assertEquals(3, actual.length);
        idx = 0;
        assertEquals("indexPage.key", actual[idx++]);
        assertEquals("index.key", actual[idx++]);
        assertEquals("key", actual[idx++]);

        actual = MessagesUtils.getMessageNameCandidates("label.key",
                new String[] { "indexPage", "index", });
        assertEquals(3, actual.length);
        idx = 0;
        assertEquals("label.indexPage.key", actual[idx++]);
        assertEquals("label.index.key", actual[idx++]);
        assertEquals("label.key", actual[idx++]);
    }
}
