package org.seasar.ymir.scope.impl;

import javax.servlet.http.HttpServletRequest;

public class RequestScope extends AbstractServletScope {
    public Object getAttribute(String name) {
        return getRequest().getAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        getRequest().setAttribute(name, value);
    }

    HttpServletRequest getRequest() {
        return (HttpServletRequest) getExternalContext().getRequest();
    }
}
