package org.seasar.ymir;

/**
 * レスポンスの種別を表すenumです。
 * 
 * @author YOKOTA Takehiko
 */
public enum ResponseType {
    /**
     * 何もせず処理をサーブレットコンテナにそのまま委譲することを表します。
     */
    PASSTHROUGH,

    /**
     * フォワード処理を行なうことを表します。
     */
    FORWARD,

    /**
     * リダイレクト処理を行なうことを表します。
     */
    REDIRECT,

    /**
     * Responseオブジェクト自身がレスポンス情報を保持していおり、
     * それをHTTPレスポンスとして出力することを表します。
     */
    SELF_CONTAINED,

    /**
     * フレームワークがHTTPレスポンスを何も返す必要がないことを表します。
     */
    VOID
}
