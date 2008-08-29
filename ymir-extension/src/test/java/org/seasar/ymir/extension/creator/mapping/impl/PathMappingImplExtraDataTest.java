package org.seasar.ymir.extension.creator.mapping.impl;

import junit.framework.TestCase;

public class PathMappingImplExtraDataTest extends TestCase {
    private PathMappingImplExtraData target_ = new PathMappingImplExtraData();

    public void testGetMinimumMatchedString() throws Exception {
        assertNull(target_.getMinimumMatchedString(null));

        assertEquals("abc", target_.getMinimumMatchedString("abc"));
        assertEquals("_aa", target_
                .getMinimumMatchedString("^_([a-zA-Z][a-zA-Z0-9]*)$"));
    }
}
