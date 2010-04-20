package org.seasar.ymir;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * {@link Response}が持つリダイレクションパスを加工するためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @see Response
 * @author YOKOTA Takehiko
 */
public interface RedirectionPathResolver {
    /**
     * 指定されたredirectパスを最終的な形式に加工して返します。
     * <p>このメソッドで加工された後に{@link HttpServletResponse#encodeRedirectURL(String)}
     * メソッドでエンコードされるため、このメソッドの中でエンコードする必要はありません。
     * </p>
     * 
     * @param path パス。
     * @param httpResponse 現在のHttpServletRequestオブジェクト。 
     * @param httpRequest 現在のHttpServletResponseオブジェクト。
     * @param request 現在のRequestオブジェクト。
     * @param response 現在のResponseオブジェクト。
     * @return 最終的なパス。
     * @since 1.0.3
     */
    String resolve(String path, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, Request request, Response response);

    /**
     * 指定されたproceedパスを最終的な形式に加工して返します。
     * <p>このメソッドは相対パスかコンテキスト相対パスだけを返すことができます。
     * </p>
     * 
     * @param path パス。
     * @param httpResponse 現在のHttpServletRequestオブジェクト。 
     * @param httpRequest 現在のHttpServletResponseオブジェクト。
     * @param request 現在のRequestオブジェクト。
     * @param response 現在のResponseオブジェクト。
     * @return 最終的なパス。
     * @since 1.0.3
     */
    String resolveForProceed(String path, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, Request request, Response response);

    /**
     * 指定されたredirect URLを最終的な形式に加工して返します。
     * <p>このメソッドに渡されるURLはリダイレクトのために加工された最終的なURLです。
     * 必要に応じてセッションIDも埋め込まれています。
     * </p>
     * 
     * @param url URL。
     * nullが指定されることはありません。
     * @param httpResponse 現在のHttpServletRequestオブジェクト。 
     * @param httpRequest 現在のHttpServletResponseオブジェクト。
     * @param request 現在のRequestオブジェクト。
     * @param response 現在のResponseオブジェクト。
     * @return 最終的なパス。
     * @since 1.0.7
     */
    String resolveURL(String url, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, Request request, Response response);
}
