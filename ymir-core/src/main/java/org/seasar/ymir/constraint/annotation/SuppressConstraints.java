package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;

/**
 * アクションの実行時に制約チェックを行なわないことを指定するためのアノテーションです。
 * <p>このアノテーションが付与されたアクションメソッドの実行時には、
 * 制約チェックが実行されなくなります。
 * </p>
 * 
 * @see org.seasar.ymir.constraint.annotation.SuppressConstraints
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SuppressConstraints {
    /**
     * 実行したくない制約チェックの種別です。
     * <p>デフォルトでは全ての種別の制約チェックが実行されません。
     * </p>
     * 
     * @return 実行したくない制約チェックの種別。
     */
    ConstraintType[] value() default { ConstraintType.VALIDATION,
        ConstraintType.PERMISSION };
}
