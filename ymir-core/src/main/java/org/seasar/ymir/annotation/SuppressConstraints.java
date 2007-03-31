package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SuppressConstraints {
    ConstraintType[] value() default { ConstraintType.VALIDATION,
        ConstraintType.PERMISSION };
}
