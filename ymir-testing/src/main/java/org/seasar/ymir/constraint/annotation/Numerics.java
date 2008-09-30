package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.annotation.Collection;

/**
 * {@link Numeric}アノテーションを複数指定するためのアノテーションです。
 * 
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@Collection
public @interface Numerics {
    /**
     * {@link Numeric}アノテーションです。
     * 
     * @return {@link Numeric}アノテーション。
     */
    Numeric[] value();
}
