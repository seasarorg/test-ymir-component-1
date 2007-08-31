package org.seasar.ymir.scope.impl;

public class RequestScope extends AbstractServletScope {
    public Object getAttribute(String name) {
        return getRequest().getAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        getRequest().setAttribute(name, value);
    }
}
