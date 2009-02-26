package org.seasar.ymir.handler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * メソッドが例外ハンドラであることを表すアノテーションです。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionHandler {
    /**
     * このアノテーションが付与されたハンドラメソッドが処理する例外のクラスです。
     * <p>この属性はこのアノテーションをPage内のメソッドに付与した時だけ意味を持ちます。
     * </p>
     * 
     * @return このアノテーションが付与されたハンドラメソッドが処理する例外のクラス。
     */
    Class<? extends Throwable> value() default Throwable.class;
}
