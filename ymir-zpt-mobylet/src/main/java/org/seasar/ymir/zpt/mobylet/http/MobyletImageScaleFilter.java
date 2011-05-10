package org.seasar.ymir.zpt.mobylet.http;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mobylet.core.http.image.MobyletImageScaleServlet;
import org.mobylet.core.util.SingletonUtils;
import org.seasar.ymir.util.ServletUtils;
import org.seasar.ymir.zpt.mobylet.image.YmirImageConfig;

public class MobyletImageScaleFilter extends MobyletImageScaleServlet implements
        Filter {
    private static final long serialVersionUID = 1L;

    private static final String PARAM_PATH = "path";

    private String path;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        path = filterConfig.getInitParameter(PARAM_PATH);
        if (path != null) {
            // ここでの指定をImageConfigに設定しておく。
            YmirImageConfig config = SingletonUtils.get(YmirImageConfig.class);
            if (config != null) {
                config.setScaleServletPath(path);
            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if (path == null || path.equals(ServletUtils.getPath(req))) {
            // pathが指定されていない場合は全てのリクエストを処理する。
            // 指定されている場合はリクエストパスがマッチする場合だけ処理する。
            doGet(req, res);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}
