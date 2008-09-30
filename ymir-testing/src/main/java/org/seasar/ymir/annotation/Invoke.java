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
}
