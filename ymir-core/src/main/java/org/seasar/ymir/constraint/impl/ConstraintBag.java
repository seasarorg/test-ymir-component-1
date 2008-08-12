package org.seasar.ymir.constraint.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Set;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConfirmationDecider;
import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.annotation.ConstraintAnnotation;

public class ConstraintBag<T extends Annotation> {
    public static final ConfirmationDecider DECIDER_ALWAYS = new ConfirmationDecider() {
        public boolean isConfirmed(Object page, Request request,
                ConstraintType type, Set<ConstraintType> suppressTypeSet) {
            return true;
        }
    };

    private Constraint<T> constraint_;

    private T annotation_;

    private AnnotatedElement element_;

    private ConfirmationDecider decider_;

    private ConstraintType type_;

    public ConstraintBag(Constraint<T> constraint, T annotation,
            AnnotatedElement element, ConfirmationDecider decider) {
        constraint_ = constraint;
        annotation_ = annotation;
        element_ = element;
        decider_ = decider;

        ConstraintAnnotation constraintAnnotation = annotation.annotationType()
                .getAnnotation(ConstraintAnnotation.class);
        type_ = constraintAnnotation.type();
    }

    public void confirm(Object page, Request request,
            Set<ConstraintType> suppressTypeSet)
            throws ConstraintViolatedException {
        if (decider_.isConfirmed(page, request, type_, suppressTypeSet)) {
            constraint_.confirm(page, request, annotation_, element_);
        }
    }

    public Constraint<T> getConstraint() {
        return constraint_;
    }
}
