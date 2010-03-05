package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.impl.FittedOnTypeConstraint;

/**
 * プロパティの型に合致していることを表す制約アノテーションです。
 * <p>このアノテーションがクラスに付与されている場合は、
 * 付与されているクラスが持つそれぞれのプロパティに対応するリクエストパラメータがプロパティの型に合致することを要求します。
 * </p>
 * <p>このアノテーションがプロパティのGetterまたはSetterに付与されている場合は、
 * 付与されているプロパティに対応するリクエストパラメータがプロパティの型に合致することを要求します。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@ConstraintAnnotation(type = ConstraintType.VALIDATION, component = FittedOnTypeConstraint.class)
public @interface FittedOnType {
    /**
     * エラーメッセージのキーです。
     * <p>通常はエラーメッセージのキーは「error.constraint.XXXX」ですが、
     * 例えばこのメンバの値を「abc」とするとキーが「error.constraint.XXXX.abc」になります。
     * </p>
     * <p>キー全体を指定したい場合は先頭に「!」をつけて下さい。
     * 例えばメンバの値を「!error.custom」とするとキーは「error.custom」になります。
     * </p>
     * 
     * @return エラーメッセージのキー。
     */
    String messageKey() default "";
}
