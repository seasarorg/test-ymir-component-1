package org.seasar.ymir.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ConstraintViolatedException;

public class ConstraintBag<T extends Annotation> {
    private Constraint<T> constraint_;

    private T annotation_;

    private AnnotatedElement element_;

    public ConstraintBag(Constraint<T> constraint, T annotation,
            AnnotatedElement element) {
        constraint_ = constraint;
        annotation_ = annotation;
        element_ = element;
    }

    @SuppressWarnings("unchecked")
    public void confirm(Object component, Request request)
            throws ConstraintViolatedException {
        constraint_.confirm(component, request, annotation_, element_);
    }

    public Constraint<T> getConstraint() {
        return constraint_;
    }
}
