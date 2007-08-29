package org.seasar.ymir.constraint;

import java.util.Set;

import org.seasar.ymir.Request;

public interface ConfirmationDecider {
    boolean isConfirmed(Object page, Request request, ConstraintType type,
            Set<ConstraintType> suppressTypeSet);
}
