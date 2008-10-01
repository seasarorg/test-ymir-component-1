package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link Inject}アノテーションを複数指定するためのアノテーションです。
 * <p>{@link Inject}アノテーションに対応するインジェクション処理は指定した順序で行なわれます。
 * </p> 
 *
 * @see Injector
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Collection
public @interface Injects {
    /**
     * {@link Inject}アノテーションです。
     * 
     * @return {@link Inject}アノテーション。
     */
    Inject[] value();
}
