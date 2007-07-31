package org.seasar.ymir.interceptor.impl;

import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;

public class AbstractYmirProcessInterceptor implements YmirProcessInterceptor {
    public static final double PRIORITY_DEFAULT = 100.;

    private double priority_ = PRIORITY_DEFAULT;

    public double getPriority() {
        return priority_;
    }

    public void setPriority(double priority) {
        priority_ = priority;
    }

    public Request requestCreated(Request request) {
        return request;
    }

    public Object componentCreated(Object component) {
        return component;
    }

    public MethodInvoker actionInvoking(Object component, MethodInvoker action,
            Request request, MethodInvoker methodInvoker)
            throws PermissionDeniedException {
        return methodInvoker;
    }
}
