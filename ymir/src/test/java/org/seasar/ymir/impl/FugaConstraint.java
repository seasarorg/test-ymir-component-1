package org.seasar.ymir.impl;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintViolatedException;

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
