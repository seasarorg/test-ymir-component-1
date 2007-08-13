package org.seasar.ymir.interceptor.impl;

import org.seasar.ymir.Action;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;

abstract public class AbstractYmirProcessInterceptor implements
        YmirProcessInterceptor {
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

    public PageComponent pageComponentCreated(PageComponent pageComponent) {
        return pageComponent;
    }

    public Action actionInvoking(Action originalAction, Request request,
            Action action) throws PermissionDeniedException {
        return action;
    }

    public String encodingRedirectURL(String url) {
        return url;
    }

    public void leavingRequest(Request request) {
    }
}
