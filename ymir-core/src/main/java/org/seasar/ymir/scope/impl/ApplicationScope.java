package org.seasar.ymir.scope.impl;

import javax.servlet.ServletContext;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.scope.Scope;

public class ApplicationScope implements Scope {
    private S2Container container_;

    public Object getAttribute(String name) {
        return getServletContext().getAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        getServletContext().setAttribute(name, value);
    }

    ServletContext getServletContext() {
        return (ServletContext) (container_.getRoot().getExternalContext()
                .getApplication());
    }

    public void setS2Container(S2Container container) {
        container_ = container;
    }
}
