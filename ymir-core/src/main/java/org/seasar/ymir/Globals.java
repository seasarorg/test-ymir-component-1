package org.seasar.ymir;

import org.seasar.ymir.annotation.RequestParameter;

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
     */
    String ATTR_LOCALE = IDPREFIX + "locale";

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
     * リクエストパラメータをPageオブジェクトにインジェクトする際に、
     * 明示的に{@link RequestParameter}アノテーションが指定されているSetter
     * にだけインジェクトするかどうかを指定するためのアプリケーションプロパティのキーです。
     */
    // TODO [YMIR-1.0][#YMIR-258] 廃止する。
    String APPKEY_CORE_REQUESTPARAMETER_STRICTINJECTION = APPKEYPREFIX_CORE
            + "requestParameter.strictInjection";
}
