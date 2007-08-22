package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.impl.TokenRequiredConstraint;
import org.seasar.ymir.impl.TokenManagerImpl;

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@ConstraintAnnotation(type = ConstraintType.VALIDATION, component = TokenRequiredConstraint.class)
public @interface TokenRequired {
    String value() default TokenManagerImpl.KEY_TOKEN;
}
