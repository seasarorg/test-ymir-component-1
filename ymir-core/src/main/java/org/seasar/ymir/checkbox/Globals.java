package org.seasar.ymir.checkbox;

/**
 * Checkboxの特殊処理に関する定数を定義するインタフェースです。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
public interface Globals extends org.seasar.ymir.Globals {
    /**
     * Checkboxの特殊処理に関するアプリケーションプロパティのキーの接頭辞です。
     */
    String APPKEYPREFIX_CORE_CHECKBOX = APPKEYPREFIX_CORE + "checkbox.";

    /**
     * チェックボックスのリクエストパラメータ名を送信するためのリクエストパラメータ名を指定するための
     * プロパティのキーです。
     */
    String APPKEY_CORE_CHECKBOX_KEY = APPKEYPREFIX_CORE_CHECKBOX + "key";

    /**
     * チェックボックスのリクエストパラメータ名を送信するためのリクエストパラメータ名のデフォルト値です。
     */
    String DEFAULT_CORE_CHECKBOX_KEY = IDPREFIX + "checkbox";
}
