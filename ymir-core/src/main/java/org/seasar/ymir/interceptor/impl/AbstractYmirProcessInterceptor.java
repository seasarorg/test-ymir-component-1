package org.seasar.ymir.interceptor.impl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.Action;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
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

    public boolean enteringRequest(ServletContext context,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            String path) {
        return true;
    }

    public Request requestCreated(Request request) {
        return request;
    }

    public PageComponent pageComponentCreated(Request request,
            PageComponent pageComponent) {
        return pageComponent;
    }

    public Action actionInvoking(Request request, Action originalAction,
            Action action) throws PermissionDeniedException {
        return action;
    }

    public Response responseCreated(Response response) {
        return response;
    }

    public void responseProcessingStarted(ServletContext context,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Request request, Response response) {
    }

    public String encodingRedirectURL(String url) {
        return url;
    }

    public void leavingRequest(Request request) {
    }
}
