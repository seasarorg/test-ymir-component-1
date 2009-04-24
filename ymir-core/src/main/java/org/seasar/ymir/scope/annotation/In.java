package org.seasar.ymir.scope.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.scope.AttributeNotFoundRuntimeException;
import org.seasar.ymir.scope.Scope;

/**
 * スコープから属性値をインジェクトする対象であることを表すアノテーションです。
 * <p>このアノテーションをPageクラスのSetterメソッドやアクションメソッドの引数に付与することで、
 * アクションメソッドの実行に先立ってスコープから属性値がインジェクトされるようになります。
 * </p> 
 *
 * @see Out
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.PARAMETER })
public @interface In {
    /**
     * スコープを表すClassオブジェクトです。
     * 
     * @return スコープを表すClassオブジェクト。
     */
    Class<? extends Scope> value() default Scope.class;

    /**
     * 属性名です。
     * <p>無指定の場合、アノテーションが付与されているメソッドに対応するプロパティ名を属性名とみなします。
     * </p>
     * 
     * @return 属性名。
     */
    String name() default "";

    /**
     * スコープを表すClassオブジェクトです。
     * <p>{@link #scopeClass()}または{@link #scopeName()}
     * のいずれかだけを指定するようにして下さい。
     * 
     * @return スコープを表すClassオブジェクト。
     */
    Class<? extends Scope> scopeClass() default Scope.class;

    /**
     * スコープ名です。
     * <p>{@link #scopeClass()}または{@link #scopeName()}
     * のいずれかだけを指定するようにして下さい。
     * 
     * @return スコープ名。
     */
    String scopeName() default "";

    /**
     * インジェクトを行なうアクションの名前です。
     * <p>このプロパティを指定した場合、
     * リクエストに対応するアクションの名前がこのプロパティで指定したアクション名のいずれかと一致する場合だけ
     * インジェクトが行なわれます。
     * このプロパティを指定しない場合は常にインジェクトが行なわれます。
     * </p>
     * 
     * @return インジェクトを行なうアクションの名前。
     */
    String[] actionName() default {};

    /**
     * 属性の値がnullである場合もインジェクトを行なうかどうかです。
     * <p>このプロパティを指定しない場合は、
     * 属性の値がnullである場合はインジェクトが行なわれません。
     * </p>
     * 
     * @return 属性の値がnullである場合もインジェクトを行なうかどうか。
     */
    boolean injectWhereNull() default false;

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
