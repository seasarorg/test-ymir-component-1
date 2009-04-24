package org.seasar.ymir.scope.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.annotation.Alias;
import org.seasar.ymir.scope.impl.PathInfoScope;
import org.seasar.ymir.scope.impl.RequestParameterScope;

/**
 * 現在のリクエストパスに付与されたpathInfoをインジェクトすることを表すアノテーションです。
 *
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.PARAMETER })
@Alias
public @interface PathInfo {
    In z_alias() default @In(PathInfoScope.class);

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
}
