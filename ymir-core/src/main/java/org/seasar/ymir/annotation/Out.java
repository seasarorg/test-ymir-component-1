package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.scope.Scope;

/**
 * スコープに属性値をアウトジェクトする対象であることを表すアノテーションです。
 * <p>このアノテーションをPageクラスのGetterメソッドに付与することで、
 * アクションの実行後に属性値がPageオブジェクトからスコープにアウトジェクトされるようになります。
 * </p> 
 * 
 * @see In
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Out {
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
     * アウトジェクトを行なうアクションの名前です。
     * <p>このプロパティを指定した場合、
     * リクエストに対応するアクションの名前がこのプロパティで指定したアクション名のいずれかと一致する場合だけ
     * アウトジェクトが行なわれます。
     * このプロパティを指定しない場合は常にアウトジェクトが行なわれます。
     * </p>
     * 
     * @return アウトジェクトを行なうアクションの名前。
     */
    String[] actionName() default {};

    /**
     * メソッドの返り値がnullである場合もアウトジェクトを行なうかどうかです。
     * <p>このプロパティを指定しない場合は、
     * メソッドの返り値がnullである場合はアウトジェクトが行なわれません。
     * </p>
     * 
     * @return メソッドの返り値がnullである場合もアウトジェクトを行なうかどうか。
     */
    boolean outjectWhereNull() default true;
}
