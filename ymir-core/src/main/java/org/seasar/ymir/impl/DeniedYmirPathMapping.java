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
        this(patternString, null);
    }

    /**
     * @since 1.0.7
     */
    public DeniedYmirPathMapping(String patternString,
            String pageComponentNameTemplate) {
        super(true, patternString, pageComponentNameTemplate);
    }
}
