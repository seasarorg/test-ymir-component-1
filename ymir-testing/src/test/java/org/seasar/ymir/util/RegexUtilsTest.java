package org.seasar.ymir.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class RegexUtilsTest extends TestCase {
    public void testToRegexPattern() throws Exception {
        assertEquals("abc", RegexUtils.toRegexPattern("abc"));
        assertEquals("abc\\.html", RegexUtils.toRegexPattern("abc.html"));
        assertEquals(
                "abc\\\\\\.\\^\\?\\*\\+\\|\\(\\)\\{\\}\\[\\]\\:\\!\\<\\>\\=",
                RegexUtils.toRegexPattern("abc\\.^?*+|(){}[]:!<>="));
        assertTrue(Pattern
                .matches(RegexUtils.toRegexPattern("abc(?:def)[a-z]"),
                        "abc(?:def)[a-z]"));
    }
    
    public void testname() throws Exception {
        Pattern p = Pattern.compile("([a-zA-Z_][a-zA-Z_0-9]*)((\\[[^]]*\\])*)");
        Matcher matcher = p.matcher("hoe[10][20]");
        matcher.find();
        System.out.println(matcher.group(3));
    }
}
