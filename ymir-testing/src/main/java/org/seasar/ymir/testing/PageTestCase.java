package org.seasar.ymir.testing;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.seasar.ymir.HttpMethod;

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

    protected FilterChain process(Object... params) throws IOException,
            ServletException {
        return process(HttpMethod.GET, params);
    }

    protected FilterChain process(HttpMethod method, Object... params)
            throws IOException, ServletException {
        return process(getPageClass(), method, params);
    }
}
