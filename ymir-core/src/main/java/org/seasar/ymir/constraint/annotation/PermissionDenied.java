package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.annotation.Alias;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.handler.annotation.ExceptionHandler;

/**
 * 権限エラーの際に呼ばれるアクションメソッドを指定するためのアノテーションです。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Alias
public @interface PermissionDenied {
    ExceptionHandler z_alias() default @ExceptionHandler(PermissionDeniedException.class);
}
