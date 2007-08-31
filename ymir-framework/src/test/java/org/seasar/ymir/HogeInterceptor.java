package org.seasar.ymir;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;

public class HogeInterceptor extends AbstractInterceptor {
    private static final long serialVersionUID = 1L;

    public Object invoke(MethodInvocation invocation) throws Throwable {
        return null;
    }
}
