package org.seasar.ymir.zpt.mobylet.http;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mobylet.core.http.ForceWrapMobyletFilter;

/**
 * ForceWrapMobyletFilterのメインの処理だけを行なうフィルタです。
 * <p>HTMLリソースなどのテキストリソースにMobyletを適用したい場合には
 * {@link ForceWrapMobyletFilter}の代わりにこのフィルタを使って下さい。
 * </p>
 * <p>このフィルタを適用する場合は、前段に必ず{@link MobyletSetUpFilter}を適用するようにして下さい。
 * </p>
 * 
 * @author yokota
 * @since 1.0.7
 */
public class MobyletProcessFilter extends ForceWrapMobyletFilter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        processFilter(chain, (HttpServletRequest) request,
                (HttpServletResponse) response);
    }
}
