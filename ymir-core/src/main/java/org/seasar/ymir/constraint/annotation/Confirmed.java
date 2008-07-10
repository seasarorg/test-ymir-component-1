package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.impl.ConfirmedConstraint;

/**
 * 対応するリクエストパラメータの値が指定されたリクエストパラメータの値と一致していることを表す制約アノテーションです。
 * <p>このアノテーションが付与されているSetterメソッドに対応するプロパティ名と同じ名前を持つリクエストパラメータと、
 * アノテーションのプロパティとして指定された名前のリクエストパラメータの値が同じであることを要求します。
 * </p>
 * <p>この制約は、例えば確認のためにメールアドレスを2回入力させるようなフォームを処理するために利用することができます。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@ConstraintAnnotation(type = ConstraintType.VALIDATION, component = ConfirmedConstraint.class)
public @interface Confirmed {
    /**
     * 比較対象のリクエストパラメータ名です。
     * 
     * @return 比較対象のリクエストパラメータ名。
     */
    String value();
}
