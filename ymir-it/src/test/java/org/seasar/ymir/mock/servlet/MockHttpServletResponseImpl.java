package org.seasar.ymir.mock.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class MockHttpServletResponseImpl extends
        org.seasar.framework.mock.servlet.MockHttpServletResponseImpl implements
        MockHttpServletResponse {
    private String redirectPath_;

    public MockHttpServletResponseImpl(HttpServletRequest request) {
        super(request);
    }

    @Override
    public void sendRedirect(String path) throws IOException {
        super.sendRedirect(path);
        redirectPath_ = path;
    }

    public String getRedirectPath() {
        return redirectPath_;
    }
}
