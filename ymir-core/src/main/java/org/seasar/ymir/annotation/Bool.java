package org.seasar.ymir.annotation;

/**
 * アノテーションの属性の型として利用するための真偽値型です。
 * 
 * @since 1.0.2
 */
public enum Bool {
    NULL, FALSE, TRUE;

    public Boolean booleanValue() {
        switch (this) {
        case NULL:
            return null;

        case FALSE:
            return Boolean.FALSE;

        case TRUE:
            return Boolean.TRUE;

        default:
            throw new RuntimeException("Can't happen!");
        }
    }
}
