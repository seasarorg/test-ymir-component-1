package org.seasar.ymir.scope.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.annotation.Alias;
import org.seasar.ymir.annotation.ElementAlias;
import org.seasar.ymir.scope.impl.RequestParameterScope;
import org.seasar.ymir.scope.impl.URIParameterScope;

/**
 * URIから抽出されたパラメータをインジェクトすることを表すアノテーションです。
 * <p>このアノテーションが付与されたSetterに対しては、
 * URIから抽出されたパラメータがインジェクトされます。
 * </p>
 *
 * @author YOKOTA Takehiko
 * @since 1.0.7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.PARAMETER })
@Alias
public @interface URIParameter {
    Populate z_alias() default @Populate(URIParameterScope.class);

    /**
     * 属性名です。
     * <p>指定した場合、アノテーションを付与したメソッドは指定した属性名の属性値をポピュレートする
     * ために使用されます。
     * 指定しなかった場合は、Apache Commons BeanUtilsのプロパティ名規則に従って
     * 属性名からメソッドが探索され、見つかったメソッドがPopulateアノテーションを持っていれば
     * 属性値がポピュレートされます。
     * </p>
     * <p>属性名を指定したPopulateアノテーションは、
     * 引数を1つだけ取るメソッドにしか指定してはいけません。
     * </p>
     * 
     * @return 属性名。
     */
    @ElementAlias("name")
    String value() default "";

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

    /**
     * 属性の値がnullである場合もポピュレートを行なうかどうかです。
     * <p>このプロパティを指定しない場合は、
     * 属性の値がnullである場合はポピュレートが行なわれません。
     * </p>
     * 
     * @return 属性の値がnullである場合もポピュレートを行なうかどうか。
     */
    boolean populateWhereNull() default false;
}
