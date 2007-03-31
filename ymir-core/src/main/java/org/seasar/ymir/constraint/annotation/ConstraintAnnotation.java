package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConstraintAnnotation {
    ConstraintType type();

    Class component();
}
