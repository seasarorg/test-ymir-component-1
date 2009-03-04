package org.seasar.ymir.mock.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public interface MockFilterChain extends FilterChain {
    boolean isCalled();
}
