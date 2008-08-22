package org.seasar.ymir.handler.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.handler.ExceptionHandler;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.kvasir.util.io.IORuntimeException;

public class PermissionDeniedExceptionHandler implements
        ExceptionHandler<PermissionDeniedException> {
    private HttpServletResponse httpResponse_;

    @Binding(bindingType = BindingType.MUST)
    public void setHttpResponse(HttpServletResponse httpResponse) {
        httpResponse_ = httpResponse;
    }

    public String handle(PermissionDeniedException t) {
        try {
            httpResponse_.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        }
    }
}
