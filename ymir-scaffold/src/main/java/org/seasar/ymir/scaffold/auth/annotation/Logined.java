package org.seasar.ymir.scaffold.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.annotation.ConstraintAnnotation;
import org.seasar.ymir.scaffold.auth.impl.LoginedConstraint;

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@ConstraintAnnotation(type = ConstraintType.PERMISSION, component = LoginedConstraint.class)
public @interface Logined {
    /**
     * ログインユーザが持っているべきロールです。
     * <p>複数指定された場合は、「ログインしていてかついずれかのロールを持っていること」という条件になります。
     * </p>
     * <p>指定がない場合は、「ログインしていること」という条件になります。
     * </p>
     * 
     * @return ログインユーザが持っているべきロール。
     */
    String[] value() default {};
}
