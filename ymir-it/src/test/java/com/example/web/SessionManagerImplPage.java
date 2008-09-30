package com.example.web;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.session.SessionManager;

public class SessionManagerImplPage {
    private SessionManager sessionManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setSessionManager(SessionManager sessionManager) {
        sessionManager_ = sessionManager;
    }

    public void _get_getSession() {
        sessionManager_.getSession(false);
    }

    public void _get_getSession2() {
        sessionManager_.getSession(true);
    }

    public void _get_invalidate() {
        sessionManager_.invalidateSession();
    }

    public void _get_invalidateAndCreate() {
        sessionManager_.invalidateAndCreateSession();
    }
}
