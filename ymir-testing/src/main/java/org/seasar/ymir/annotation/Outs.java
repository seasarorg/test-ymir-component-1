package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link Out}アノテーションを複数指定するためのアノテーションです。
 * <p>{@link Out}アノテーションに対応するアウトジェクション処理は指定した順序で行なわれます。
 * </p> 
 *
 * @see Out
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Collection
public @interface Outs {
    /**
     * {@link Out}アノテーションです。
     * 
     * @return {@link Out}アノテーション。
     */
    Out[] value();
}
