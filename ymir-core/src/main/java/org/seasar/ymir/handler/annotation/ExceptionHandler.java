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
     * <p>この要素指定はPageクラスが持つ例外ハンドラについてのみ有効です。
     * グローバル例外ハンドラに対して指定することはできません。
     * </p>
     * 
     * @return このアノテーションが付与されたハンドラメソッドが処理する例外のクラス。
     */
    Class<? extends Throwable> value() default Throwable.class;

    /**
     * アクションの名前です。
     * <p>この要素を指定した場合、
     * リクエストに対応するアクションの名前がこの要素で指定したアクション名のいずれかと一致する場合だけ
     * このアノテーションが付与されたメソッドがハンドラメソッドと見なされます。
     * この要素を指定しない場合は常にハンドラメソッドと見なされます。
     * </p>
     * <p>この要素指定はPageクラスが持つ例外ハンドラについてのみ有効です。
     * グローバル例外ハンドラに対して指定することはできません。
     * </p>
     * 
     * @return アクションの名前。
     * @since 1.0.3
     */
    String[] actionName() default {};
}
