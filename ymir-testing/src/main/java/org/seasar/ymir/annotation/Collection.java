package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * アノテーションのコレクションであることを表すためのメタアノテーションです。
 * <p>Javaでは同一のアノテーションを複数個付与することができません。
 * そのため、同一のアノテーションを複数個持つコレクションアノテーションを作成してそれを付与することが行なわれます。
 * このアノテーションは、コレクションアノテーションを定義するためのメタアノテーションです。
 * </p>
 * <p>コレクションアノテーションを作成する時はこのアノテーションを付与して下さい。
 * また、コレクションアノテーションの<code>value</code>プロパティを定義して型を要素アノテーションの配列にして下さい。
 * </p>
 * 
 * @since 0.9.6
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Collection {
}
