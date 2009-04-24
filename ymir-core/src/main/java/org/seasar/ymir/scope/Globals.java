package org.seasar.ymir.scope;

/**
 * scopeに関する定数を定義するインタフェースです。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
public interface Globals extends org.seasar.ymir.Globals {
    /**
     * scopeに関するアプリケーションプロパティのキーの接頭辞です。
     */
    String APPKEYPREFIX_CORE_SCOPE = APPKEYPREFIX_CORE + "scope.";

    String APPKEY_CORE_SCOPE_DEFAULTSCOPENAME = APPKEYPREFIX_CORE_SCOPE
            + "defaultScopeName";

    String DEFAULT_CORE_SCOPE_DEFAULTSCOPENAME = "requestScope";
}
