package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.annotation.Alias;
import org.seasar.ymir.annotation.Collection;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.handler.annotation.ExceptionHandler;
import org.seasar.ymir.scope.annotation.Populate;
import org.seasar.ymir.scope.impl.RequestParameterScope;

/**
 * バリデーションに失敗した際に呼ばれるアクションメソッドを指定するためのアノテーションです。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Alias
public @interface ValidationFailed {
    ExceptionHandler z_alias() default @ExceptionHandler(ValidationFailedException.class);
}
