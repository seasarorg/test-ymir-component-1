package org.seasar.ymir.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.seasar.ymir.HttpServletResponseFilter;

public class AsIsResponseFilter extends HttpServletResponseWrapper implements
        HttpServletResponseFilter {

    private boolean propagateContentType_ = true;

    public AsIsResponseFilter(HttpServletResponse response) {
        super(response);
    }

    public void commit() throws IOException {
    }

    public void setPropagateContentType(boolean propagateContentType) {
        this.propagateContentType_ = propagateContentType;
    }

    public void setContentType(String type) {
        if (propagateContentType_) {
            getResponse().setContentType(type);
        }
    }
}
