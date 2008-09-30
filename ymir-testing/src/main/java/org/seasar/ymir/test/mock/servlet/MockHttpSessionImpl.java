package org.seasar.ymir.test.mock.servlet;

import javax.servlet.ServletContext;

public class MockHttpSessionImpl extends
        org.seasar.framework.mock.servlet.MockHttpSessionImpl {

    private static final long serialVersionUID = 1L;

    private MockHttpServletRequestImpl request_;

    public MockHttpSessionImpl(ServletContext servletContext,
            MockHttpServletRequestImpl request) {
        super(servletContext);
        request_ = request;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        request_.clearSession();
    }
}
