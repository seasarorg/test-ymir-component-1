package org.seasar.ymir.scope.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.S2Container;
import org.seasar.ymir.scope.Scope;

abstract public class AbstractServletScope implements Scope {
    protected S2Container container_;

    public void setS2Container(S2Container container) {
        container_ = container;
    }

    protected ExternalContext getExternalContext() {
        return container_.getRoot().getExternalContext();
    }

    protected HttpSession getSession() {
        return getSession(true);
    }

    protected HttpServletRequest getRequest() {
        return (HttpServletRequest) getExternalContext().getRequest();
    }

    protected HttpSession getSession(boolean create) {
        return getRequest().getSession(create);
    }
}
