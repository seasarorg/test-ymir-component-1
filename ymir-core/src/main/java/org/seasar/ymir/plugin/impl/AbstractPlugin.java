package org.seasar.ymir.plugin.impl;

import java.lang.annotation.Annotation;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.Action;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.plugin.Plugin;

abstract public class AbstractPlugin<A extends Annotation> implements Plugin<A> {
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
            String path, A annotataion) {
        return true;
    }

    public Request requestCreated(Request request, A annotataion) {
        return request;
    }

    public PageComponent pageComponentCreated(Request request,
            PageComponent pageComponent, A annotataion) {
        return pageComponent;
    }

    public Action actionInvoking(Request request, Action originalAction,
            Action action, A annotataion) throws PermissionDeniedException {
        return action;
    }

    public Response responseCreated(Response response, A annotataion) {
        return response;
    }

    public void responseProcessingStarted(ServletContext context,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Request request, Response response, A annotataion) {
    }

    public String encodingRedirectURL(String url, A annotataion) {
        return url;
    }

    public void leavingRequest(Request request, A annotataion) {
    }

    public void leftRequest(A annotataion) {
    }
}
