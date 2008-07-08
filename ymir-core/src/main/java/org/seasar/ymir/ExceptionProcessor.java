package org.seasar.ymir;

import org.seasar.ymir.handler.ExceptionHandler;

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
     * 例外発生時のレスポンスとなる標準の画面テンプレートが格納されているディレクトリへのパスです。
     */
    String PATH_EXCEPTION_TEMPLATE = "/WEB-INF/template/exception/";

    /**
     * 例外発生時のレスポンスとなる標準の画面テンプレートの拡張子です。
     */
    String SUFFIX_EXCEPTION_TEMPLATE = ".html";

    /**
     * 例外をハンドリングするデフォルトの{@link ExceptionHandler}コンポーネント名の接頭辞です。
     */
    String NAMEPREFIX_DEFAULT = "default_";

    /**
     * 例外をハンドリングする{@link ExceptionHandler}コンポーネントクラス名の接尾辞です。
     */
    String SUFFIX_HANDLER = "Handler";

    /**
     * 例外をハンドリングした{@link ExceptionHandler}コンポーネントを画面テンプレート等から取得できるように
     * {@link Request}オブジェクトに属性として設定する際の属性名です。
     */
    String ATTR_HANDLER = "handler";

    /**
     * 指定された例外を処理します。
     * 
     * @param request Requestオブジェクト。
     * @param t 発生した例外。
     * @return 例外を処理した結果のResponseオブジェクト。
     */
    Response process(Request request, Throwable t);
}
