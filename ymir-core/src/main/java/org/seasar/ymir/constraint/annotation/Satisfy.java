package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.impl.RequiredConstraint;
import org.seasar.ymir.constraint.impl.SatisfyConstraint;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ConstraintAnnotation(type = ConstraintType.VALIDATION, component = SatisfyConstraint.class)
public @interface Satisfy {
    Class<?>[] value();
}
