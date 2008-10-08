package org.seasar.ymir.token.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.annotation.ConstraintAnnotation;
import org.seasar.ymir.token.constraint.impl.TokenRequiredConstraint;

/**
 * トランザクショントークンが一致することを表す制約アノテーションです。
 * <p>リクエストパラメータに指定されているトランザクショントークンと
 * セッションに保存されているトランザクショントークンが一致することを要求します。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@ConstraintAnnotation(type = ConstraintType.VALIDATION, component = TokenRequiredConstraint.class)
public @interface TokenRequired {
    /**
     * トークンキーの名前です。
     * @return トークンキーの名前。
     */
    String value() default "";
}
