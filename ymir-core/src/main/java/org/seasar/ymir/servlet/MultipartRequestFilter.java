package org.seasar.ymir.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

/**
 * マルチパートのHTTPリクエストを前処理するためのFilterクラスです。
 * <p>このFilterを使うことで、enctypeが"multipart/form-data"であるようなフォームからsubmitされた
 * HTTPリクエストを適切に処理することができます。
 * </p>
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class MultipartRequestFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) res;

        if (ServletFileUpload.isMultipartContent(new ServletRequestContext(
                httpRequest))) {
            httpRequest = new MultipartServletRequest(httpRequest);
        }
        chain.doFilter(httpRequest, httpResponse);
    }
}
