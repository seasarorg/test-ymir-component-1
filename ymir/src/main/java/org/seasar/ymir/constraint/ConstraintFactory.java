package org.seasar.ymir.constraint;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public interface ConstraintFactory<T extends Annotation> {

    Constraint getConstraint(T annotation, AnnotatedElement element);
}
