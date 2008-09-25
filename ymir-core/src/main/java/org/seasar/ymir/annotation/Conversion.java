package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.TypeConversionManager;

/**
 * 型変換に関するヒントを与えるためのアノテーションに付与するメタアノテーションです。
 * <p>このメタアノテーションが付与されたアノテーションは型変換のためのヒントとして
 * {@link TypeConversionManager#convert(Object, Class, java.lang.annotation.Annotation[])}
 * に渡されます。
 * </p>
 * 
 * @since 1.0.0
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Conversion {
}
