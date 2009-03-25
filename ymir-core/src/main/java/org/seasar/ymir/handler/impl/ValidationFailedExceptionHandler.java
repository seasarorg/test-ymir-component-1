package org.seasar.ymir.handler.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.ymir.Response;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.handler.annotation.ExceptionHandler;
import org.seasar.ymir.response.PassthroughResponse;

public class ValidationFailedExceptionHandler {
    @ExceptionHandler
    public void handle(ValidationFailedException t) {
    }
}
