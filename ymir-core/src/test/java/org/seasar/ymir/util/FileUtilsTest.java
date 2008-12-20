package org.seasar.ymir.util;

import junit.framework.TestCase;

public class FileUtilsTest extends TestCase {
    public void testIsRelativePath() throws Exception {
        assertTrue(FileUtils.isRelativePath("a"));
        assertTrue(FileUtils.isRelativePath("a/b/c"));
        assertTrue(FileUtils.isRelativePath("a\\b\\c"));
        assertFalse(FileUtils.isRelativePath("c:\\a"));
        assertFalse(FileUtils.isRelativePath("C:\\a"));
        assertFalse(FileUtils.isRelativePath("c:\\a\\b\\c"));
        assertFalse(FileUtils.isRelativePath("c:\\a/b/c"));
        assertFalse(FileUtils.isRelativePath("/a"));
        assertFalse(FileUtils.isRelativePath("/a/b/c"));
        assertFalse(FileUtils.isRelativePath(""));
    }
}
