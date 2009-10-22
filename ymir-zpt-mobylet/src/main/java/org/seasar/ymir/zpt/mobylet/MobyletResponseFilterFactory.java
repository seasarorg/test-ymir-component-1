package org.seasar.ymir.zpt.mobylet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.HttpServletResponseFilter;
import org.seasar.ymir.HttpServletResponseFilterFactory;
import org.seasar.ymir.Updater;
import org.seasar.ymir.impl.HttpServletResponseFilterFactoryImpl;

/**
 * Mobylet連携専用の{@link HttpServletResponseFilterFactory}です。
 *
 * @since 1.0.7
 */
public class MobyletResponseFilterFactory extends
        HttpServletResponseFilterFactoryImpl {
    @Override
    public HttpServletResponseFilter newAsIsResponseFilter(
            HttpServletResponse response) {
        HttpServletResponseFilter filter = super
                .newAsIsResponseFilter(response);
        filter.setPropagateContentType(false);
        return filter;
    }

    @Override
    public HttpServletResponseFilter newUpdaterResponseFilter(
            HttpServletRequest request, HttpServletResponse response,
            Updater[] updaters) {
        HttpServletResponseFilter filter = super.newUpdaterResponseFilter(
                request, response, updaters);
        filter.setPropagateContentType(false);
        return filter;
    }
}
