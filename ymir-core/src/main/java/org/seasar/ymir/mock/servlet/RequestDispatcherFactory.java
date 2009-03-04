package org.seasar.ymir.mock.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public interface RequestDispatcherFactory {
    RequestDispatcher newInstance(String path);
}
