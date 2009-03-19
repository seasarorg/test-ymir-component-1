package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * エイリアスアノテーションの要素の別名を定義するためのアノテーションです。
 * <p>エイリアスアノテーションの要素に付与することで、エイリアスアノテーションの要素とオリジナルのアノテーションの要素の対応を変えることができます。
 * </p>
 * <p>例えば次のように書くことで、オリジナルのアノテーションのvalue要素の値としてエイリアスアノテーションのaliased要素の値が使われるようになります：</p>
 * <pre>
 *     @ElementAlias("value")
 *     String aliased();
 * </pre>
 * 
 * @since 1.0.3
 * @author YOKOTA Takehiko
 * @see Alias
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ElementAlias {
    /**
     * 対応付けたいオリジナルのアノテーションの要素の名前です。
     * <p>このアノテーションが付与された要素をオリジナルのアノテーションのどの要素に対応付けるかを指定して下さい。
     * </p>
     * 
     * @return 対応付けたいオリジナルのアノテーションの要素の名前。
     */
    String value();
}
