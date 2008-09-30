package org.seasar.ymir.testing.mock.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

public class MockHttpServletRequestImpl extends
        org.seasar.framework.mock.servlet.MockHttpServletRequestImpl implements
        MockHttpServletRequest {
    private ServletContext servletContext_;

    private MockHttpSession session_;

    public MockHttpServletRequestImpl(ServletContext servletContext,
            String servletPath) {
        this(servletContext, servletPath, null);
    }

    public MockHttpServletRequestImpl(ServletContext servletContext,
            String servletPath, MockHttpSession session) {
        super(servletContext, servletPath);
        servletContext_ = servletContext;
        session_ = session;
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

    protected void setSession(MockHttpSession session) {
        session_ = session;
    }

    protected void clearSession() {
        session_ = null;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return new MockRequestDispatcherImpl(path);
    }
}
