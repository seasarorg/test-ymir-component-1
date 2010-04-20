package org.seasar.ymir.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.FollowingURLResolver;
import org.seasar.ymir.Request;

public class FollowingURLResolverImpl implements FollowingURLResolver {
    public boolean isResolved(String url, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, Request request) {
        return true;
    }

    public String resolveURL(String url, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, Request request) {
        return url;
    }
}
