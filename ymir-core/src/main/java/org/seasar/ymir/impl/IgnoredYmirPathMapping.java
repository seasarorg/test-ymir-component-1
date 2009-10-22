package org.seasar.ymir.impl;

/**
 * Ymirが処理を行なわないパスのパターンを表すPathMappingです。
 * 
 * @author skirnir
 * @since 1.0.7
 */
public class IgnoredYmirPathMapping extends YmirPathMapping {
    public IgnoredYmirPathMapping(String patternString) {
        super(true, patternString);
    }
}
