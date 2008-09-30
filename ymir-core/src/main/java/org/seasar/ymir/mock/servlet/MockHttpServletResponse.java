package org.seasar.ymir.testing.mock.servlet;

public interface MockHttpServletResponse extends
        org.seasar.framework.mock.servlet.MockHttpServletResponse {
    String getRedirectPath();
}
