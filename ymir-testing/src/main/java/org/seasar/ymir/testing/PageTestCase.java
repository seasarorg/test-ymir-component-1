package org.seasar.ymir.testing;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.mock.servlet.MockFilterChain;

abstract public class PageTestCase<P> extends YmirTestCase {
    private Class<P> pageClass_;

    @SuppressWarnings("unchecked")
    public PageTestCase(P... ps) {
        pageClass_ = (Class<P>) ps.getClass().getComponentType();
    }

    protected Class<P> getPageClass() {
        return pageClass_;
    }

    protected P getPage() {
        return getComponent(getPageClass());
    }

    public FilterChain process() throws IOException, ServletException {
        return process(getPageClass());
    }

    public FilterChain process(String param, Object... params)
            throws IOException, ServletException {
        return process(getPageClass(), param, params);
    }

    public FilterChain process(HttpMethod method) throws IOException,
            ServletException {
        return process(getPageClass(), method);
    }

    public FilterChain process(HttpMethod method, String param,
            Object... params) throws IOException, ServletException {
        return process(getPageClass(), method, param, params);
    }

    public FilterChain process(RequestInitializer initializer)
            throws IOException, ServletException {
        return process(getPageClass(), initializer);
    }

    public FilterChain process(RequestInitializer initializer, String param,
            Object... params) throws IOException, ServletException {
        return process(getPageClass(), initializer, param, params);
    }

    public FilterChain process(MockFilterChain chain) throws IOException,
            ServletException {
        return process(getPageClass(), chain);
    }

    public FilterChain process(MockFilterChain chain, String param,
            Object... params) throws IOException, ServletException {
        return process(getPageClass(), chain, param, params);
    }

    public FilterChain process(HttpMethod method,
            RequestInitializer initializer, MockFilterChain chain)
            throws IOException, ServletException {
        return process(getPageClass(), method, initializer, chain);
    }

    public FilterChain process(HttpMethod method,
            RequestInitializer initializer, MockFilterChain chain,
            String param, Object... params) throws IOException,
            ServletException {
        return process(getPageClass(), method, initializer, chain, param,
                params);
    }
}
