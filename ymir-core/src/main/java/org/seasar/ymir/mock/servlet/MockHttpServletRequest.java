package org.seasar.ymir.mock.servlet;

public interface MockHttpServletRequest extends
        org.seasar.framework.mock.servlet.MockHttpServletRequest {
    void setRequestDispatcherFactory(
            RequestDispatcherFactory requestDispatcherFactory);
}
