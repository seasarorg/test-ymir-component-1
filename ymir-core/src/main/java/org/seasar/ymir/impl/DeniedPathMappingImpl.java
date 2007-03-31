package org.seasar.ymir.impl;

public class DeniedPathMappingImpl extends PathMappingImpl {

    public DeniedPathMappingImpl(String patternString) {
        super(true, patternString, null, null, null, null, null);
    }
}
