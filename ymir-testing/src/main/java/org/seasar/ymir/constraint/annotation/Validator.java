package org.seasar.ymir.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Pageオブジェクトに対して制約チェックを行なう際に、バリデータとして実行するメソッドを指定するためのアノテーションです。
 * 
 * @author YOKOTA Takehiko
 */
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
