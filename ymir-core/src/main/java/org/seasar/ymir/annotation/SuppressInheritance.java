package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.framework.container.factory.AnnotationHandler;

/**
 * アノテーションの継承を抑制するためのアノテーションです。
 * <p>{@link AnnotationHandler}のinheritedプロパティがtrueになっている場合、
 * 要素からアノテーションを取得する時には要素の祖先要素に付与されているアノテーションも
 * 取得されます。
 * このアノテーションを付与すると、
 * このアノテーションを付与したレベルよりも祖先要素のアノテーションを取得しないようになります。
 * </p>
 * <p>このアノテーションが直接付与された要素に関してのみ有効であることに注意して下さい。
 * 例えばこのアノテーションがクラスに付与されていても、
 * クラスが持つメソッドのアノテーションを取得する挙動には影響を与えません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.7
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SuppressInheritance {
}
