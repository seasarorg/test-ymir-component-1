package org.seasar.ymir.impl;

public class DeniedYmirPathMapping extends YmirPathMapping {
    public DeniedYmirPathMapping(String patternString) {
        super(true, patternString, null);
    }
}
