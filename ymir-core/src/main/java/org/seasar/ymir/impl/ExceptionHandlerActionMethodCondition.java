package org.seasar.ymir.impl;

public class ExceptionHandlerActionMethodCondition {
    private Class<?> exceptionClass_;

    private String actionName_;

    public ExceptionHandlerActionMethodCondition(Class<?> exceptionClass,
            String actionName) {
        exceptionClass_ = exceptionClass;
        actionName_ = actionName;
    }

    public Class<?> getExceptionClass() {
        return exceptionClass_;
    }

    public String getActionName() {
        return actionName_;
    }
}
