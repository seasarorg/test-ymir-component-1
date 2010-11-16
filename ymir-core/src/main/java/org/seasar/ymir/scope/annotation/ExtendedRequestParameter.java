package org.seasar.ymir.scope.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.annotation.Alias;
import org.seasar.ymir.annotation.ElementAlias;
import org.seasar.ymir.scope.impl.ExtendedRequestParameterScope;

/**
 * 拡張リクエストパラメータをインジェクトすることを表すアノテーションです。
 * <p>このアノテーションが付与されたSetterに対しては、
 * JSONやAMFで渡されるような拡張リクエストパラメータがインジェクトされます。
 * </p>
 *
 * @author YOKOTA Takehiko
 * @since 1.0.7
 * @see ExtendedRequestParameterScope
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.PARAMETER })
@Alias
public @interface ExtendedRequestParameter {
    In z_alias() default @In(ExtendedRequestParameterScope.class);

    /**
     * 属性名です。
     * <p>指定した場合、アノテーションを付与したメソッドは指定した属性名の属性値をインジェクトする
     * ために使用されます。
     * 指定しなかった場合は、アノテーションが付与されたメソッドに対応するプロパティの名前と同じ名前の値がインジェクトされます。
     * </p>
     * 
     * @return 属性名。
     */
    @ElementAlias("name")
    String value() default "";

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
}
