package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.impl.DatetimeConstraint;
import org.seasar.ymir.converter.annotation.TypeConversionHint;

/**
 * ページのアクションを呼び出す前に確認すべき制約を表すアノテーションを保持しているメソッドであることを表すアノテーションです。
 * <p>Pageクラスのメソッドにこのアノテーションを付与しておくと、アクションの呼び出しに先立って、
 * Pageクラスに付与されたメソッドやSetterメソッド付与されている制約アノテーションとともに、
 * このアノテーションが付与されたメソッドに付与されている制約アノテーションに関しても制約の確認が行なわれます。
 * </p>
 * <p>このアノテーションが付与されたメソッドがbooleanを返り値とするメソッドの場合、返り値がfalseの場合は制約は無視されます。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ConstraintHolder {
}
