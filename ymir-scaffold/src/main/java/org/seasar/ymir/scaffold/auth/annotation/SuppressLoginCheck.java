package org.seasar.ymir.scaffold.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.scaffold.auth.LoginCheckConstraint;

/**
 * ログインチェックを抑制するためのアノテーションです。
 * <p>このアノテーションが付与されている場合、ログインチェックが抑制されます。
 * 
 * @author skirnir
 * @see Logined
 * @see LoginCheckConstraint
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE })
public @interface SuppressLoginCheck {
}
