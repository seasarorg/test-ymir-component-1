package org.seasar.ymir.extension.zpt;

import junit.framework.TestCase;

public class AnalyzerUtilsTest extends TestCase {
    public void testIsValidVariableName() throws Exception {
        assertFalse(AnalyzerUtils.isValidVariableName(""));
        assertTrue(AnalyzerUtils.isValidVariableName("abC9"));
        assertFalse(AnalyzerUtils.isValidVariableName("9ab"));
        assertFalse(AnalyzerUtils.isValidVariableName("abc-d"));
    }
}
