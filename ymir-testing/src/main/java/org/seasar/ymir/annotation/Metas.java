package org.seasar.ymir.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 複数のメタデータを付与するためのアノテーションです。
 * <p>このアノテーションは主にフレームワークの拡張モジュールによって利用されます。
 * </p>
 *
 * @see Meta
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Metas {
    /**
     * メタデータです。
     * 
     * @return メタデータ。
     */
    Meta[] value();
}
