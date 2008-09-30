package org.seasar.ymir.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.framework.aop.annotation.Interceptor;

/**
 * リクエストのプロトコルがHTTPでなかった場合にHTTPであるURLに
 * リダイレクトさせるようなレスポンスを返すためのアノテーションです。
 * <p>このアノテーションは返り値がStringであるアクションメソッドに付与して下さい。
 * </p>
 * 
 * @author yokota
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@Interceptor
public @interface HTTP {
    int port() default 80;
}
