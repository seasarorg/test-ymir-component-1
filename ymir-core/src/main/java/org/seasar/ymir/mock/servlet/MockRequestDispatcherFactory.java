package org.seasar.ymir.mock.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class MockRequestDispatcherFactory implements RequestDispatcherFactory {
    public RequestDispatcher newInstance(String path, HttpServletRequest request) {
        return new MockRequestDispatcherImpl(path, request);
    }
}
