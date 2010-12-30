package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.extension.tx.RequiredInterceptor;
import org.seasar.extension.tx.annotation.RequiredTx;
import org.seasar.ymir.aop.interceptor.impl.YmirRequiredInterceptor;

/**
 * トランザクション境界を無効にするためのアノテーションです。
 * <p>Seasar2の{@code j2ee.requiredTx}インターセプタが適用されたメソッドに付与することで
 * {@code j2ee.requiredTx}インターセプタによるトランザクションの開始を抑制することができます。
 * </p>
 * 
 * @since 1.0.7
 * @author YOKOTA Takehiko
 * @see RequiredInterceptor
 * @see YmirRequiredInterceptor
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
public @interface SuppressRequiredTx {
}
