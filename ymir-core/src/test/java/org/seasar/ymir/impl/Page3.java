package org.seasar.ymir.impl;

import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.handler.annotation.ExceptionHandler;

public class Page3 {
    @ExceptionHandler
    public String handle() throws ValidationFailedException {
        throw new ValidationFailedException();
    }
}
