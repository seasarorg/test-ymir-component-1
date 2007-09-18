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

    // FIXME Seasar-2.4.17の不具合回避のためのworkaround。Seasar-2.4.18からは不要。
    @Override
    protected String adjustPath(String path) {
        if (path != null && path.length() > 0 && path.charAt(0) == '/') {
            return path.substring(1);
        }
        return path;
    }
}
