package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Pageオブジェクトに対して制約チェックを行なう際に、バリデータとして実行するメソッドを指定するためのアノテーションです。
 * <p>このアノテーションは互換性のために残されています。
 * このアノテーションの代わりに{@link org.seasar.ymir.constraint.annotation.Validator}
 * アノテーションを使用して下さい。
 * </p>
 * 
 * @see org.seasar.ymir.constraint.annotation.Validator
 * @author YOKOTA Takehiko
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Validator {
    /**
     * バリデータメソッドを呼び出すアクションの名前です。
     * <p>実行しようとしているアクションの名前がここで指定されたアクション名のいずれかに一致する場合だけ、
     * バリデータメソッドが実行されます。
     * </p>
     * 
     * @return バリデータメソッドを呼び出すアクションの名前。
     */
    String[] value() default {};
}
