package org.seasar.ymir.message;

import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;

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

    public void testAdd_複数カテゴリに同時にエントリを追加できること() throws Exception {
        target_.add("a+b", new Note("value"));

        assertEquals(1, target_.getNotes("a").length);
        assertEquals(1, target_.getNotes("b").length);
        assertEquals(1, target_.getNotes().length);
    }

    public void testAdd_Note_Strings_複数カテゴリに同時にエントリを追加できること() throws Exception {
        target_.add(new Note("value"), "a", "b");

        assertEquals(1, target_.getNotes("a").length);
        assertEquals(1, target_.getNotes("b").length);
        assertEquals(1, target_.getNotes().length);
    }
}
