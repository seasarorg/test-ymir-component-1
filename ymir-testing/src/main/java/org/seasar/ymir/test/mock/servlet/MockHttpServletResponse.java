package org.seasar.ymir.test.mock.servlet;

public interface MockHttpServletResponse extends
        org.seasar.framework.mock.servlet.MockHttpServletResponse {
    String getRedirectPath();
}