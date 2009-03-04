package org.seasar.ymir.mock.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MockFilterChainImpl implements MockFilterChain {
    private boolean called_;

    public void doFilter(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        called_ = true;
    }

    public boolean isCalled() {
        return called_;
    }
}
