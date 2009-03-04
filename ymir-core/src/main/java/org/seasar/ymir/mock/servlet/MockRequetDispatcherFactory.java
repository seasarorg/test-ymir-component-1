package org.seasar.ymir.mock.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MockRequetDispatcherFactory implements RequestDispatcherFactory {
    public RequestDispatcher newInstance(String path) {
        return new MockRequestDispatcherImpl(path);
    }
}
