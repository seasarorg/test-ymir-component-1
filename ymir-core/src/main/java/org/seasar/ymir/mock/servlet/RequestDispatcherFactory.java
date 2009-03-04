package org.seasar.ymir.mock.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public interface RequestDispatcherFactory {
    RequestDispatcher newInstance(String path, HttpServletRequest request);
}
