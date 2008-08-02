package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link In}アノテーションを複数指定するためのアノテーションです。
 * <p>{@link In}アノテーションに対応するインジェクション処理は指定した順序で行なわれます。
 * </p> 
 *
 * @see In
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Ins {
    /**
     * {@link In}アノテーションです。
     * 
     * @return {@link In}アノテーション。
     */
    In[] value();
}
