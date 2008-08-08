package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 同じ種類の制約を複数付与するためのアノテーションに付与するメタアノテーションです。
 * <p>同じ種類の制約を複数付与するためのアノテーションクラスにはこのメタアノテーションを付与する必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Deprecated
public @interface ConstraintsAnnotation {
}
