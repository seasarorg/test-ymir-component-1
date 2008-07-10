package org.seasar.ymir.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * メタデータを付与するためのアノテーションです。
 * <p>このアノテーションは主にフレームワークの拡張モジュールによって利用されます。
 * </p>
 * 
 * @see Metas
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Meta {
    /**
     * メタデータの名前です。
     * 
     * @return メタデータの名前。
     */
    String name();

    /**
     * メタデータの内容です。
     * 
     * @return メタデータの内容。
     */
    String value();
}
