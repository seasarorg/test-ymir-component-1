package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.impl.RequiredConstraint;

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@ConstraintAnnotation(type = ConstraintType.VALIDATION, component = RequiredConstraint.class)
public @interface Required {
    String[] value() default {};

    boolean completely() default false;

    boolean allowWhitespace() default true;
}
