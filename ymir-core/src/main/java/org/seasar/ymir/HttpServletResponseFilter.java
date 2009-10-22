package org.seasar.ymir;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * HttpServletResponseへの出力を遅延させるためのインタフェースです。
 * <p>{@link #commit()}メソッドを呼び出すことで、
 * 遅延していたレスポンスが実際にHttpServletResponseオブジェクトに渡されます。
 * </p>
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要はありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface HttpServletResponseFilter extends HttpServletResponse {
    /**
     * レスポンスの状態を確定させます。
     * <p>このメソッドを呼び出さないうちは実際のHttpServletResponseにレスポンスが書き込まれません。
     * </p>
     * 
     * @throws IOException エラーが発生した場合。
     */
    void commit() throws IOException;

    /**
     * 設定されたコンテントタイプの情報を内包するHttpServletResponseに伝播させるかどうかを設定します。
     * <p>デフォルトでは伝播するようになっています。伝播させたくない場合にfalseを指定して下さい。
     * </p>
     * 
     * @param propagateContentType コンテントタイプの情報を内包するHttpServletResponseに伝播させるかどうか。
     * @since 1.0.7
     */
    void setPropagateContentType(boolean propagateContentType);
}
