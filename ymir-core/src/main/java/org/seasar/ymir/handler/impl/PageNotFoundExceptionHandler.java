package org.seasar.ymir.handler.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.PageNotFoundException;
import org.seasar.ymir.handler.ExceptionHandler;
import org.seasar.kvasir.util.io.IORuntimeException;

public class PageNotFoundExceptionHandler implements
        ExceptionHandler<PageNotFoundException> {
    private HttpServletResponse httpResponse_;

    public void setHttpResponse(HttpServletResponse httpResponse) {

        httpResponse_ = httpResponse;
    }

    public String handle(PageNotFoundException t) {

        try {
            httpResponse_.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        }
    }
}
