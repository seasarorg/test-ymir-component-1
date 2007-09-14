package org.seasar.ymir.test.mock.servlet;

import javax.servlet.RequestDispatcher;

public class MockServletContextImpl extends
        org.seasar.framework.mock.servlet.MockServletContextImpl {
    private static final long serialVersionUID = 2458242111693537876L;

    public MockServletContextImpl(String path) {
        super(path);
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return new MockRequestDispatcherImpl(path);
    }
}
