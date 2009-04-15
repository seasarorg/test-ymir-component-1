package org.seasar.ymir;

import org.seasar.ymir.locale.LocaleManager;

/**
 * フレームワーク関連の定数を定義するインタフェースです。
 * 
 * @author YOKOTA Takehiko
 */
public interface Globals {
    /**
     * フレームワークが管理するリクエストパラメータや属性の名前等につける接尾辞です。
     */
    String IDPREFIX = "org.seasar.ymir.";

    /**
     * コンポーネントをコンテナに自動登録する際の基準となるクラスの名前です。
     */
    String LANDMARK_CLASSNAME = IDPREFIX + "landmark.Landmark";

    /**
     * 現在のコンテキストに関連付けられているロケールオブジェクトを保持する属性の名前です。
     * @deprecated 代わりに{@link LocaleManager#ATTR_LOCALE}を使って下さい。
     */
    String ATTR_LOCALE = LocaleManager.ATTR_LOCALE;

    /**
     * デフォルトのメッセージプロパティファイルから接尾辞を除いたものです。
     */
    String NAME_MESSAGES = "messages";

    /**
     * デフォルトのメッセージプロパティファイルの名前です。
     */
    String MESSAGES = NAME_MESSAGES + ".xproperties";

    /**
     * フレームワークが管理するアプリケーションプロパティのキーにつける接頭辞です。
     */
    String APPKEYPREFIX_CORE = "core.";

    /**
     * Responseの文字列表現において、リッチなパス表現を利用可能にするかどうかを指定するためのプロパティのキーです。
     */
    String APPKEY_CORE_RESPONSE_STRATEGY_RICHPATHEXPRESSIONAVAILABLE = APPKEYPREFIX_CORE
            + "response.strategy.richPathExpressionAvailable";

    /**
     * ExceptionHandlerインタフェースを有効にするかどうかを指定するためのプロパティのキーです。
     */
    String APPKEY_CORE_HANDLER_EXCEPTIONHANDLERINTERFACE_ENABLE = APPKEYPREFIX_CORE
            + "handler.exceptionHandlerInterface.enable";

    String APPKEY_LANDMARK = "landmark";
}
