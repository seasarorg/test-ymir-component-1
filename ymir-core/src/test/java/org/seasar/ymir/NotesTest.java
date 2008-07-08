package org.seasar.ymir;

import junit.framework.TestCase;

public class NotesTest extends TestCase {
    private Notes target_ = new Notes();

    public void testContainsValue() throws Exception {
        assertFalse(target_.containsValue("hoe"));

        target_.add(new Note("hoe"));

        assertTrue(target_.containsValue("hoe"));
    }

    public void testContainsValue_nullも検索できること() throws Exception {
        assertFalse(target_.containsValue(null));

        target_.add(new Note(null));

        assertTrue(target_.containsValue(null));
    }
}
