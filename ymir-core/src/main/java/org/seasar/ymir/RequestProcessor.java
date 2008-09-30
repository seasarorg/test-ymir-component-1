package org.seasar.ymir;

/**
 * HTTPリクエストを処理するためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface RequestProcessor {
    /**
     * 代わりにConstraintInterceptor#ACTION_VALIDATIONFAILEDを使って下さい。
     */
    @Deprecated
    String ACTION_VALIDATIONFAILED = "_validationFailed";

    /**
     * 代わりにConstraintInterceptor#ACTION_PERMISSIONDENIEDを使って下さい。
     */
    @Deprecated
    String ACTION_PERMISSIONDENIED = "_permissionDenied";

    /**
     * アクション名に対応するメソッドがない場合のデフォルトのアクション名です。
     */
    @Deprecated
    String ACTION_DEFAULT = "_default";

    /**
     * レンダリングのためにフレームワークによって呼び出されるメソッドの名前です。
     */
    @Deprecated
    String METHOD_RENDER = "_render";

    /**
     * {@link Request}に属性としてPageオブジェクトをバインドする際のキー文字列です。
     */
    String ATTR_SELF = "self";

    /**
     * {@link Request}に属性としてNotesオブジェクトをバインドする際のキー文字列です。
     */
    String ATTR_NOTES = "notes";

    /**
     * {@link Request}に属性として{@link PageComponent}オブジェクトをバインドする際のキー文字列です。
     */
    String ATTR_PAGECOMPONENT = "pageComponent";

    /**
     * RequestオブジェクトからResponseオブジェクトを構築します。
     * 
     * @param request 現在のRequestオブジェクト。
     * @return 構築したResponseオブジェクト。nullを返すことはありません。
     */
    Response process(Request request);

    /**
     * サーブレットのインクルード処理を行なう前に属性を退避します。
     * 
     * @param attributeContainer コンテナ。
     * @return 退避した情報が格納されているオブジェクト。
     */
    Object backupForInclusion(AttributeContainer attributeContainer);

    /**
     * サーブレットのインクルード処理が終了した後に退避情報を復元します。
     * 
     * @param attributeContainer 退避情報の復元先である属性コンテナ。
     * @param backupped 退避した情報が格納されているオブジェクト。
     */
    void restoreForInclusion(AttributeContainer attributeContainer,
            Object backupped);
}
