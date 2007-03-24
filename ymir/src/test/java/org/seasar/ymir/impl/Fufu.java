package org.seasar.ymir.impl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.annotation.ConstraintAnnotation;
import org.seasar.ymir.constraint.ConstraintType;

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@ConstraintAnnotation(type = ConstraintType.PERMISSION, factory = FufuConstraintFactory.class)
public @interface Fufu {

    String value();
}
