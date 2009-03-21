package org.seasar.ymir.history;

/**
 * historyに関する定数を定義するインタフェースです。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
public interface Globals extends org.seasar.ymir.Globals {
    /**
     * historyに関するアプリケーションプロパティのキーの接頭辞です。
     */
    String APPKEYPREFIX_CORE_HISTORY = APPKEYPREFIX_CORE + "history.";

    String APPKEY_CORE_HISTORY_AUTORECORDING = APPKEYPREFIX_CORE_HISTORY
            + "autoRecording";

    boolean DEFAULT_CORE_HISTORY_AUTORECORDING = false;
}
