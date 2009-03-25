package org.seasar.ymir;

/**
 * 例外発生時の処理を行なうためのインタフェースです。
 * <p>フレームワークがHTTPリクエストを受け取ってからレスポンスを構築し終わるまでの間に発生した例外を処理するためのインタフェースです。
 * </p>
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface ExceptionProcessor {
    /**
     * 例外をハンドリングするデフォルトのExceptionHandlerコンポーネント名の接頭辞です。
     */
    String NAMEPREFIX_DEFAULT = "default_";

    /**
     * 例外をハンドリングするExceptionHandlerコンポーネントクラス名の接尾辞です。
     */
    String SUFFIX_HANDLER = "Handler";

    /**
     * 例外をハンドリングしたExceptionHandlerコンポーネントを画面テンプレート等から取得できるように
     * {@link Request}オブジェクトに属性として設定する際の属性名です。
     */
    String ATTR_HANDLER = "handler";

    /**
     * 例外をハンドリングしたExceptionHandlerコンポーネントがページコンポーネントかグローバルハンドラコンポーネントか
     * を画面テンプレート等から取得できるように
     * {@link Request}オブジェクトに属性として設定する際の属性名です。
     * <p>属性値はBoolean型です。</p>
     * @since 1.0.2
     */
    String ATTR_HANDLER_GLOBAL = "handler.global";

    /**
     * スローされた例外を画面テンプレート等から取得できるように
     * {@link Request}オブジェクトに属性として設定する際の属性名です。
     * <p>属性値はThrowable型です。</p>
     */
    String ATTR_EXCEPTION = "exception";

    /**
     * 指定された例外を処理します。
     * 
     * @param request Requestオブジェクト。
     * @param t 発生した例外。
     * @param useHandlerInPage Page内ハンドラを使用するかどうか。
     * @return 例外を処理した結果のResponseオブジェクト。
     */
    Response process(Request request, Throwable t, boolean useHandlerInPage);
}
