package org.seasar.ymir.redirection.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.Globals;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.redirection.ScopeIdManager;
import org.seasar.ymir.util.StringUtils;
import org.seasar.ymir.window.WindowManager;

abstract public class AbstractScopeIdManager implements ScopeIdManager {
    public static final String KEY_SCOPEID = Globals.IDPREFIX
            + "redirection.id";

    protected ApplicationManager applicationManager_;

    private String scopeIdKey_ = KEY_SCOPEID;

    @Binding(bindingType = BindingType.MUST)
    public final void setApplicationManager(
            ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setScopeIdKey(String scopeIdKey) {
        scopeIdKey_ = scopeIdKey;
    }

    public String getScopeIdKey() {
        return scopeIdKey_;
    }

    protected S2Container getS2Container() {
        return applicationManager_.findContextApplication().getS2Container();
    }

    protected HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) getS2Container().getComponent(
                HttpServletRequest.class);
    }

    protected HttpServletResponse getHttpServletResponse() {
        return (HttpServletResponse) getS2Container().getComponent(
                HttpServletResponse.class);
    }

    protected Request getRequest() {
        return (Request) getS2Container().getComponent(Request.class);
    }

    protected Response getResponse() {
        return (Response) getS2Container().getComponent(Response.class);
    }

    protected String getScopeIdForNextRequest() {
        return StringUtils.getScopeKey(getRequest());
    }
}
