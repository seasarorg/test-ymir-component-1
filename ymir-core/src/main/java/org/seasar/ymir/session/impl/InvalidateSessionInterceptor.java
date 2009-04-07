package org.seasar.ymir.session.impl;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.aop.annotation.HTTP;
import org.seasar.ymir.session.SessionManager;
import org.seasar.ymir.session.annotation.InvalidateSession;

public class InvalidateSessionInterceptor extends AbstractInterceptor {
    private static final long serialVersionUID = 1L;

    private SessionManager sessionManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setSessionManager(SessionManager sessionManager) {
        sessionManager_ = sessionManager;
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object returned = invocation.proceed();
        if (invocation.getMethod().getAnnotation(InvalidateSession.class)
                .continuation()) {
            sessionManager_.invalidateAndCreateSession();
        } else {
            sessionManager_.invalidateSession();
        }
        return returned;
    }
}
