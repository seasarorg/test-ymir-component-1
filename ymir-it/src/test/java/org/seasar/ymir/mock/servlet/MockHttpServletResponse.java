package org.seasar.ymir.mock.servlet;

public interface MockHttpServletResponse extends
        org.seasar.framework.mock.servlet.MockHttpServletResponse {
    String getRedirectPath();
}
