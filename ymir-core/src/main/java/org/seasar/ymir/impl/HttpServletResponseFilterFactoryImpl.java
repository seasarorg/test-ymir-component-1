package org.seasar.ymir.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.HttpServletResponseFilter;
import org.seasar.ymir.HttpServletResponseFilterFactory;
import org.seasar.ymir.Updater;

/**
 * {@link HttpServletResponseFilterFactory}の標準実装です。
 * 
 * @author skirnir
 * @since 1.0.7
 */
public class HttpServletResponseFilterFactoryImpl implements
        HttpServletResponseFilterFactory {
    public HttpServletResponseFilter newAsIsResponseFilter(
            HttpServletResponse response) {
        return new AsIsResponseFilter(response);
    }

    public HttpServletResponseFilter newUpdaterResponseFilter(
            HttpServletRequest request, HttpServletResponse response,
            Updater[] updaters) {
        return new UpdaterResponseFilter(request, response, updaters);
    }
}
