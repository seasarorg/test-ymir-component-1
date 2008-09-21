package org.seasar.ymir.impl;

/**
 * @deprecated このメソッドは互換性のために残されています。
 * 新しくアプリケーションを構築する場合は代わりに{@link DeniedYmirPathMapping}
 * の使用を検討して下さい。
 * @author yokota
 */
public class DeniedPathMappingImpl extends PathMappingImpl {
    public DeniedPathMappingImpl(String patternString) {
        super(true, patternString, null, null, null, null, null, null);
    }
}
