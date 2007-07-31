package org.seasar.ymir.scope.impl;

import javax.servlet.ServletContext;

public class ApplicationScope extends AbstractServletScope {
    public Object getAttribute(String name) {
        return getServletContext().getAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        getServletContext().setAttribute(name, value);
    }

    ServletContext getServletContext() {
        return (ServletContext) getExternalContext().getApplication();
    }
}
