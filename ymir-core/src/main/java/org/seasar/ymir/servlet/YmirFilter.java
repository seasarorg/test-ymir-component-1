package org.seasar.ymir.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.cms.pluggable.ThreadContext;
import org.seasar.ymir.AttributeContainer;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.HttpServletResponseFilter;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.WrappingRuntimeException;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.impl.HttpServletRequestAttributeContainer;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;
import org.seasar.ymir.util.ServletUtils;

/**
 * フレームワークの動作させるための{@link Filter}クラスです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class YmirFilter implements Filter {
    private Dispatcher dispatcher_;

    private ServletContext context_;

    private Ymir ymir_;

    public void init(FilterConfig config) throws ServletException {
        context_ = config.getServletContext();
        ymir_ = (Ymir) context_.getAttribute(YmirListener.ATTR_YMIR);

        String dispatcher = config.getInitParameter("dispatcher");
        if (dispatcher != null) {
            dispatcher_ = Dispatcher.valueOf(dispatcher.toUpperCase());
        }
    }

    public void destroy() {
        context_ = null;
        ymir_ = null;
        dispatcher_ = null;
    }

    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) res;
        HttpMethod method;
        try {
            method = HttpMethod.enumOf(httpRequest.getMethod());
        } catch (IllegalArgumentException ex) {
            throw new ServletException("Unknown HTTP method: "
                    + httpRequest.getMethod());
        }

        ymir_.process(context_, httpRequest, httpResponse,
                getDispatcher(httpRequest), ServletUtils
                        .getNativePath(httpRequest), method, chain);
    }

    protected Dispatcher getDispatcher(ServletRequest request) {
        if (dispatcher_ != null) {
            return dispatcher_;
        } else {
            return ServletUtils.getDispatcher(request);
        }
    }
}
