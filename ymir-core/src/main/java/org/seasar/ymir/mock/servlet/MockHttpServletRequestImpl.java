package org.seasar.ymir.mock.servlet;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Path;

public class MockHttpServletRequestImpl extends
        org.seasar.framework.mock.servlet.MockHttpServletRequestImpl implements
        MockHttpServletRequest {
    private ServletContext servletContext_;

    private String requestPath_;

    private MockHttpSession session_;

    private boolean queryStringApplied_;

    private RequestDispatcherFactory requestDispatcherFactory_ = new MockRequestDispatcherFactory();

    private String servletPath_;

    @Deprecated
    public MockHttpServletRequestImpl(ServletContext servletContext,
            String requestPath) {
        this(servletContext, "GET", requestPath);
    }

    public MockHttpServletRequestImpl(ServletContext servletContext,
            HttpMethod method, String requestPath) {
        this(servletContext, method.name(), requestPath);
    }

    public MockHttpServletRequestImpl(ServletContext servletContext,
            String method, String requestPath) {
        this(servletContext, method, requestPath, null);
    }

    public MockHttpServletRequestImpl(ServletContext servletContext,
            String method, String requestPath, MockHttpSession session) {
        super(servletContext, new Path(requestPath).getTrunk());
        setMethod(method.toUpperCase());
        servletContext_ = servletContext;
        requestPath_ = requestPath;
        session_ = session;
        Path path = new Path(requestPath);
        setServletPath(path.getTrunk());
        if (!method.equalsIgnoreCase("POST")) {
            setQueryString(path.getQueryString());
        }
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
        return requestDispatcherFactory_.newInstance(path, this);
    }

    public void setRequestDispatcherFactory(
            RequestDispatcherFactory requestDispatcherFactory) {
        requestDispatcherFactory_ = requestDispatcherFactory;
    }

    @Override
    public String getParameter(String name) {
        if (!queryStringApplied_) {
            applyQueryString();
        }
        return super.getParameter(name);
    }

    void applyQueryString() {
        queryStringApplied_ = true;

        Path path = new Path(requestPath_, getCharacterEncoding());
        for (Map.Entry<String, String[]> entry : path.getParameterMap()
                .entrySet()) {
            for (String value : entry.getValue()) {
                addParameter(entry.getKey(), value);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map getParameterMap() {
        if (!queryStringApplied_) {
            applyQueryString();
        }
        return super.getParameterMap();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getParameterNames() {
        if (!queryStringApplied_) {
            applyQueryString();
        }
        return super.getParameterNames();
    }

    @Override
    public String[] getParameterValues(String name) {
        if (!queryStringApplied_) {
            applyQueryString();
        }
        return super.getParameterValues(name);
    }

    @Override
    public void addParameter(String name, String value) {
        if (!queryStringApplied_) {
            applyQueryString();
        }
        super.addParameter(name, value);
    }

    @Override
    public void addParameter(String name, String[] values) {
        if (!queryStringApplied_) {
            applyQueryString();
        }
        super.addParameter(name, values);
    }

    @Override
    public void setParameter(String name, String value) {
        if (!queryStringApplied_) {
            applyQueryString();
        }
        super.setParameter(name, value);
    }

    @Override
    public void setParameter(String name, String[] values) {
        if (!queryStringApplied_) {
            applyQueryString();
        }
        super.setParameter(name, values);
    }

    @Override
    public String getServletPath() {
        return servletPath_;
    }

    public void setServletPath(String servletPath) {
        servletPath_ = servletPath;
    }
}
