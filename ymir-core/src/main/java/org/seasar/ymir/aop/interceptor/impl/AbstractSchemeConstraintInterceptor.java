package org.seasar.ymir.aop.interceptor.impl;

import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.Response;
import org.seasar.ymir.response.RedirectResponse;
import org.seasar.ymir.response.scheme.impl.RedirectStrategy;
import org.seasar.ymir.util.ServletUtils;

abstract public class AbstractSchemeConstraintInterceptor extends
        AbstractInterceptor {
    private static final long serialVersionUID = 1L;

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

        String url = ServletUtils
                .constructRequestURL(httpRequest, scheme, port);
        Class<?> returnType = invocation.getMethod().getReturnType();
        if (returnType == String.class) {
            return RedirectStrategy.SCHEME + ":" + url;
        } else if (returnType.isAssignableFrom(Response.class)) {
            return new RedirectResponse(url);
        } else {
            throw new IllegalClientCodeRuntimeException(
                    "The return type must be String or supertype of Response: "
                            + invocation.getMethod());
        }
    }

    abstract protected String getScheme();

    abstract protected int getPort(MethodInvocation invocation);

    HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) applicationManager_
                .findContextApplication().getS2Container().getComponent(
                        HttpServletRequest.class);
    }
}
