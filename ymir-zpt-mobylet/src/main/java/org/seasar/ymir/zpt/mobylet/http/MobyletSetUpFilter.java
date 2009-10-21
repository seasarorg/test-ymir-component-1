package org.seasar.ymir.zpt.mobylet.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mobylet.core.http.ForceWrapMobyletFilter;

/**
 * ForceWrapMobyletFilterの前処理、後処理だけを行なうフィルタです。
 * 
 * @author yokota
 * @since 1.0.7
 */
public class MobyletSetUpFilter extends ForceWrapMobyletFilter {
    protected void processFilter(FilterChain chain, HttpServletRequest request,
            HttpServletResponse response) throws UnsupportedEncodingException,
            IOException, ServletException {
        chain.doFilter(request, response);
    }
}
