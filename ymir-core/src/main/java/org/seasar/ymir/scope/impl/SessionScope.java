package org.seasar.ymir.scope.impl;

import javax.servlet.http.HttpSession;

public class SessionScope extends AbstractServletScope {
    public Object getAttribute(String name) {
        HttpSession session = getSession(false);
        if (session == null) {
            return null;
        } else {
            return session.getAttribute(name);
        }
    }

    public void setAttribute(String name, Object value) {
        getSession().setAttribute(name, value);
    }
}
