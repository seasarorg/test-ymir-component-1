package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link Matched}アノテーションを複数指定するためのアノテーションです。
 * 
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@ConstraintsAnnotation
public @interface Matcheds {
    /**
     * {@link Matched}アノテーションです。
     * 
     * @return {@link Matched}アノテーション。
     */
    Matched[] value();
}
