package org.seasar.ymir.aop.interceptor.impl;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.ymir.aop.annotation.HTTP;
import org.seasar.ymir.util.ServletUtils;

public class HTTPInterceptor extends AbstractSchemeConstraintInterceptor {
    private static final long serialVersionUID = 1L;

    @Override
    protected int getPort(MethodInvocation invocation) {
        return invocation.getMethod().getAnnotation(HTTP.class).port();
    }

    @Override
    protected String getScheme() {
        return ServletUtils.SCHEME_HTTP;
    }
}
