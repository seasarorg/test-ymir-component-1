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
}
