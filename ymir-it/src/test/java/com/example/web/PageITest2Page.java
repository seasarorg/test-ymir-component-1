package com.example.web;

import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.impl.RequestScope;

public class PageITest2Page {
    private String message;

    public String getMessage() {
        return message;
    }

    @In(scopeClass = RequestScope.class, actionName = "_get.*")
    public void setMessage(String message) {
        this.message = message;
    }

    public void _get() {
    }

    public void _get_ok() {
    }

    public void _post() {
    }
}
