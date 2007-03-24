package org.seasar.ymir.extension.impl;

import org.seasar.ymir.ConstraintViolatedException;
import org.seasar.ymir.Request;

public class FugaConstraint implements NamedConstraint {

    private String name_;

    public FugaConstraint(String name) {
        name_ = name;
    }

    public String getName() {
        return name_;
    }

    public void confirm(Object component, Request request)
            throws ConstraintViolatedException {
    }
}
