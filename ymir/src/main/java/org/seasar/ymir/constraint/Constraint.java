package org.seasar.ymir.constraint;

import org.seasar.ymir.Request;

public interface Constraint {

    String PREFIX_MESSAGEKEY = "error.constraint.";

    void confirm(Object component, Request request)
            throws ConstraintViolatedException;
}
