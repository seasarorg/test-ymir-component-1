package org.seasar.ymir.constraint;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Constraint;

public interface ConstraintFactory<T extends Annotation> {

    Constraint getConstraint(T annotation, AnnotatedElement element);
}
