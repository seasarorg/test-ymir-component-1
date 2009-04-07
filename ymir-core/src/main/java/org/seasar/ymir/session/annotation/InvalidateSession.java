package org.seasar.ymir.session.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.framework.aop.annotation.Interceptor;
import org.seasar.ymir.session.SessionManager;

/**
 * セッションを無効化するためのアノテーションです。
 * <p>このアノテーションが付与されたメソッドの処理が終わったタイミングで{@link SessionManager#invalidateSession()}
 * または{@link SessionManager#invalidateAndCreateSession()}が呼び出されます。
 * ただし例外がスローされてメソッドから抜けた場合はセッションの無効化処理は呼び出されません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@Interceptor
public @interface InvalidateSession {
    /**
     * セッションを無効化した後にセッションを継続するかどうかです。
     * <p>この要素がtrueである場合は{@link SessionManager#invalidateAndCreateSession()}が呼び出されます。
     * falseである場合は{@link SessionManager#invalidateSession()}が呼び出されます。
     * </p>
     * <p>デフォルト値はfalseです。
     * </p>
     * 
     * @return セッションを無効化した後にセッションを継続するかどうか。
     * @see SessionManager#invalidateSession()
     * @see SessionManager#invalidateAndCreateSession()
     */
    boolean continuation() default false;
}
