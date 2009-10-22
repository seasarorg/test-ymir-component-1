package org.seasar.ymir.impl;

/**
 * 直接リクエストされることが許可されていないパスのパターンを表すPathMappingです。
 * <p>forwardやincludeなど、内部でディスパッチされた場合は処理が許可されます。
 * </p>
 * 
 * @author skirnir
 */
public class DeniedYmirPathMapping extends YmirPathMapping {
    public DeniedYmirPathMapping(String patternString) {
        super(true, patternString, null);
    }
}
