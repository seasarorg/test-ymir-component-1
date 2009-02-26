package org.seasar.ymir.handler.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.ymir.PageNotFoundRuntimeException;
import org.seasar.ymir.handler.annotation.ExceptionHandler;

public class PageNotFoundRuntimeExceptionHandler {
    private HttpServletResponse httpResponse_;

    @Binding(bindingType = BindingType.MUST)
    public void setHttpResponse(HttpServletResponse httpResponse) {
        httpResponse_ = httpResponse;
    }

    @ExceptionHandler
    public String handle(PageNotFoundRuntimeException t) {
        try {
            httpResponse_.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        }
    }
}
