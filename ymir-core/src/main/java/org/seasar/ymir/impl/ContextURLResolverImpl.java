package org.seasar.ymir.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.ContextURLResolver;
import org.seasar.ymir.Request;

public class ContextURLResolverImpl implements ContextURLResolver {
    public boolean isResolved(String url, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, Request request) {
        return true;
    }

    public String resolveURL(String url, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, Request request) {
        return url;
    }
}
