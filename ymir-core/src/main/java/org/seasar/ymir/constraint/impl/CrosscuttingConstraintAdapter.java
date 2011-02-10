package org.seasar.ymir.constraint.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.CrosscuttingConstraint;

class CrosscuttingConstraintAdapter implements Constraint<Annotation> {
    private CrosscuttingConstraint constraint;

    public CrosscuttingConstraintAdapter(CrosscuttingConstraint constraint) {
        this.constraint = constraint;
    }

    public void confirm(Object component, Request request,
            Annotation annotation, AnnotatedElement element)
            throws ConstraintViolatedException {
        constraint.confirm(component, request);
    }
}
