package org.seasar.ymir.aop.interceptor.impl;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.extension.tx.RequiredInterceptor;
import org.seasar.ymir.annotation.SuppressRequiredTx;

/**
 * アノテーションによる抑制も可能な{@link RequiredInterceptor}実装です。
 * <p>このインターセプタが適用されたメソッドに{@link SuppressRequiredTx}アノテーションを指定することで
 * このインターセプタの動作を抑制することができます。
 * </p>
 * <p>カスタマイザなどで一律にメソッドに{@link RequiredInterceptor}を適用した場合に
 * 一部のメソッドへのインターセプタの適用を抑制できるようにこのクラスが導入されました。
 * </p>
 * 
 * @since 1.0.7
 * @author YOKOTA Takehiko
 * @see SuppressRequiredTx
 */
public class YmirRequiredInterceptor extends RequiredInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (invocation.getMethod()
                .isAnnotationPresent(SuppressRequiredTx.class)) {
            return invocation.proceed();
        } else {
            return super.invoke(invocation);
        }
    }
}
