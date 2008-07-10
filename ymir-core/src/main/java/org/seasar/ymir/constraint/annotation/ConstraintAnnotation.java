package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ConstraintType;

/**
 * 制約を付与するためのアノテーションに付与するメタアノテーションです。
 * <p>制約を付与するためのアノテーションクラスにはこのメタアノテーションを付与する必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConstraintAnnotation {
    /**
     * 制約のタイプです。
     * 
     * @return 制約のタイプ。
     */
    ConstraintType type();

    /**
     * 制約チェックを行なうクラスです。
     * 
     * @return 制約チェックを行なうクラス。
     */
    Class<? extends Constraint<?>> component();
}
