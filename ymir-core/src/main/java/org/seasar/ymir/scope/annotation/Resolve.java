package org.seasar.ymir.scope.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.scope.AttributeNotFoundRuntimeException;
import org.seasar.ymir.scope.Scope;

/**
 * スコープから属性値を取り出してアクションメソッドの引数とすることを表すアノテーションです。
 * <p>このアノテーションをアクションメソッドの引数に付与することで、アクションの実行に先立って
 * スコープから属性値が取り出されます。
 * </p> 
 *
 * @see In
 * @author YOKOTA Takehiko
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Resolve {
    /**
     * スコープを表すClassオブジェクトです。
     * <p>{@link #value()}または{@link #scopeClass()}または{@link #scopeName()}
     * のいずれかだけを指定するようにして下さい。
     * 
     * @return スコープを表すClassオブジェクト。
     */
    Class<? extends Scope> value() default Scope.class;

    /**
     * 属性名です。
     * 
     * @return 属性名。
     */
    String name() default "";

    /**
     * スコープを表すClassオブジェクトです。
     * <p>{@link #value()}または{@link #scopeClass()}または{@link #scopeName()}
     * のいずれかだけを指定するようにして下さい。
     * 
     * @return スコープを表すClassオブジェクト。
     */
    Class<? extends Scope> scopeClass() default Scope.class;

    /**
     * スコープ名です。
     * <p>{@link #value()}または{@link #scopeClass()}または{@link #scopeName()}
     * のいずれかだけを指定するようにして下さい。
     * 
     * @return スコープ名。
     */
    String scopeName() default "";

    /**
     * 属性の値が非nullである必要があるかどうかです。
     * <p>このプロパティをtrueにすると、属性の値がnullである場合は
     * {@link AttributeNotFoundRuntimeException}がスローされます。
     * </p>
     * 
     * @return 属性の値が非nullである必要があるかどうか。
     */
    boolean required() default false;
}
