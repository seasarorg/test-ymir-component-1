package org.seasar.ymir.constraint;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Request;

public interface Constraint<T extends Annotation> {
    String PREFIX_MESSAGEKEY = "error.constraint.";

    void confirm(Object component, Request request, T annotation,
            AnnotatedElement element) throws ConstraintViolatedException;
}
