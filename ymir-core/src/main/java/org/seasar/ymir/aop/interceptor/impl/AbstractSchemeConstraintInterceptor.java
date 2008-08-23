package org.seasar.ymir.aop.interceptor.impl;

import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.response.scheme.impl.RedirectStrategy;
import org.seasar.ymir.util.ServletUtils;

abstract public class AbstractSchemeConstraintInterceptor extends
        AbstractInterceptor {
    private ApplicationManager applicationManager_;

    @Binding(bindingType = BindingType.MUST)
    final public void setApplicationManager(
            ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        HttpServletRequest httpRequest = getHttpServletRequest();
        String scheme = getScheme();
        int port = getPort(invocation);
        if (scheme.equals(httpRequest.getScheme())) {
            return invocation.proceed();
        }

        return RedirectStrategy.SCHEME + ":"
                + ServletUtils.constructRequestURL(httpRequest, scheme, port);
    }

    abstract protected String getScheme();

    abstract protected int getPort(MethodInvocation invocation);

    HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) applicationManager_
                .findContextApplication().getS2Container().getComponent(
                        HttpServletRequest.class);
    }
}
