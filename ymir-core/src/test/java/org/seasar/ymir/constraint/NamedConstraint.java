package org.seasar.ymir.constraint;

import java.lang.annotation.Annotation;

import org.seasar.ymir.constraint.Constraint;

public interface NamedConstraint<T extends Annotation> extends Constraint<T> {
    String getName();
}
