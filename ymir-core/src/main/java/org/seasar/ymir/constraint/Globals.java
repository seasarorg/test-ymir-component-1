package org.seasar.ymir.constraint;

/**
 * constraintに関する定数を定義するインタフェースです。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.2
 */
public interface Globals extends org.seasar.ymir.Globals {
    /**
     * constraintに関するアプリケーションプロパティのキーの接頭辞です。
     */
    String APPKEYPREFIX_CORE_CONSTRAINT = APPKEYPREFIX_CORE + "constraint.";

    String APPKEY_CORE_CONSTRAINT_VALIDATIONFAILEDMETHOD_ENABLE = APPKEYPREFIX_CORE_CONSTRAINT
            + "validationFailedMethod.enable";

    String APPKEY_CORE_CONSTRAINT_PERMISSIONDENIEDMETHOD_ENABLE = APPKEYPREFIX_CORE_CONSTRAINT
            + "permissionDeniedMethod.enable";
}
