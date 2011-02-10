package org.seasar.ymir.constraint;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Set;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.annotation.ConstraintAnnotation;

public class ConstraintBag<T extends Annotation> {
    private Constraint<T> constraint_;

    private T annotation_;

    private AnnotatedElement annotatedElement_;

    private ConfirmationDecider confirmationDecider_;

    private ConstraintType type_;

    public ConstraintBag(Constraint<T> constraint, T annotation,
            AnnotatedElement annotatedElement,
            ConfirmationDecider confirmationDecider) {
        constraint_ = constraint;
        annotation_ = annotation;
        annotatedElement_ = annotatedElement;
        confirmationDecider_ = confirmationDecider;

        ConstraintAnnotation constraintAnnotation = annotation.annotationType()
                .getAnnotation(ConstraintAnnotation.class);
        type_ = constraintAnnotation.type();
    }

    public ConstraintBag(Constraint<T> constraint, ConstraintType type,
            ConfirmationDecider confirmationDecider) {
        constraint_ = constraint;
        confirmationDecider_ = confirmationDecider;
        type_ = type;
    }

    public void confirm(Object page, Request request,
            Set<ConstraintType> suppressTypeSet)
            throws ConstraintViolatedException {
        if (confirmationDecider_.isConfirmed(page, request, type_,
                suppressTypeSet)) {
            constraint_.confirm(page, request, annotation_, annotatedElement_);
        }
    }

    public Constraint<T> getConstraint() {
        return constraint_;
    }

    public T getAnnotation() {
        return annotation_;
    }

    public AnnotatedElement getAnnotatedElement() {
        return annotatedElement_;
    }

    public ConfirmationDecider getConfirmationDecider() {
        return confirmationDecider_;
    }

    public ConstraintType getType() {
        return type_;
    }
}
