package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.scope.Scope;

/**
 * スコープから属性値をポピュレートする対象であることを表すアノテーションです。
 * <p>フレームワークは、指定されたスコープに存在する全ての属性について
 * Apache Commons BeanUtilsのプロパティ名規則に従ってPageオブジェクトに値を設定しますが、
 * この処理の対象としてもよいメソッドにこのアノテーションを付与して下さい。
 * このアノテーションが付与されないメソッドはポピュレーション処理から保護されます。
 * </p>
 * <p>スコープからのポピュレーション処理はスコープからのインジェクション処理よりも先に行なわれます。
 * </p> 
 *
 * @see In
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Populate {
    /**
     * スコープを表すClassオブジェクトです。
     * 
     * @return スコープを表すClassオブジェクト。
     */
    Class<? extends Scope> value() default Scope.class;

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
     * ポピュレートを行なうアクションの名前です。
     * <p>このプロパティを指定した場合、
     * リクエストに対応するアクションの名前がこのプロパティで指定したアクション名のいずれかと一致する場合だけ
     * ポピュレートが行なわれます。
     * このプロパティを指定しない場合は常にポピュレートが行なわれます。
     * </p>
     * 
     * @return ポピュレートを行なうアクションの名前。
     */
    String[] actionName() default {};
}
