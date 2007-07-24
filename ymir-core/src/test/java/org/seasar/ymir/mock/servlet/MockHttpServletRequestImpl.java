package org.seasar.ymir.mock.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.seasar.framework.mock.servlet.MockHttpSession;

public class MockHttpServletRequestImpl extends
        org.seasar.framework.mock.servlet.MockHttpServletRequestImpl {
    private ServletContext servletContext_;

    private MockHttpSession session_;

    public MockHttpServletRequestImpl(ServletContext servletContext,
            String servletPath) {
        super(servletContext, servletPath);
        servletContext_ = servletContext;
    }

    @Override
    public String getRequestedSessionId() {
        if (session_ != null) {
            return session_.getId();
        }
        return null;
    }

    @Override
    public HttpSession getSession(boolean create) {
        if (session_ != null) {
            return session_;
        }
        if (create) {
            session_ = new MockHttpSessionImpl(servletContext_, this);
        }
        if (session_ != null) {
            session_.access();
        }
        return session_;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        if (session_ != null) {
            return session_.isValid();
        }
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return session_ != null;
    }

    void clearSession() {
        session_ = null;
    }
}