package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.Phase;

/**
 * HTTPリクエストの処理フェーズに関連付けてPageオブジェクトのメソッドを実行することを指定するためのアノテーションです。
 * 
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Invoke {
    /**
     * メソッドを実行するフェーズです。
     * @return メソッドを実行するフェーズ。
     */
    Phase value();

    /**
     * メソッドを実行するアクションの名前です。
     * <p>このプロパティを指定した場合、
     * リクエストに対応するアクションの名前がこのプロパティで指定したアクション名のいずれかと一致する場合だけ
     * メソッドが実行されます。
     * このプロパティを指定しない場合は常にメソッドが実行されます。
     * </p>
     * 
     * @return メソッドを実行するアクションの名前。
     * @since 1.0.7
     */
    String[] actionName() default {};
}
