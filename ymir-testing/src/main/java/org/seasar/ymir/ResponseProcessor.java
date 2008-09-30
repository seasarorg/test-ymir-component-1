package org.seasar.ymir;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 抽象的なレスポンスである{@link Response}オブジェクトからHTTPレスポンスを構築するためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface ResponseProcessor {
    /**
     * ResponseオブジェクトからHTTPレスポンスを構築します。
     * 
     * @param context ServletContextオブジェクト。
     * @param httpRequest HttpServletRequestオブジェクト。
     * @param httpResponse HttpServletResponseオブジェクト。
     * @param request Requestオブジェクト。
     * @param response Responseオブジェクト。
     * @return 構築したHTTPレスポンスを表すHttpServletResponseFilterオブジェクト。
     * @throws IOException I/Oエラーが発生した場合。
     * @throws ServletException サーブレットエラーが発生した場合。
     */
    HttpServletResponseFilter process(ServletContext context,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Request request, Response response) throws IOException,
            ServletException;
}
