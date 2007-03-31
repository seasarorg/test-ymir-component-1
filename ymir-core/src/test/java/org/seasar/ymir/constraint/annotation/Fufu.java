package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.annotation.ConstraintAnnotation;
import org.seasar.ymir.constraint.impl.FufuConstraint;

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@ConstraintAnnotation(type = ConstraintType.PERMISSION, component = FufuConstraint.class)
public @interface Fufu {
    String value();
}
