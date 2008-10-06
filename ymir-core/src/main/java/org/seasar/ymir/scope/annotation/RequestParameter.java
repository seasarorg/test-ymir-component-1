package org.seasar.ymir.scope.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.annotation.Alias;
import org.seasar.ymir.scope.impl.RequestParameterScope;

/**
 * リクエストパラメータまたはURIから抽出されたパラメータをインジェクトすることを表すアノテーションです。
 * <p>このアノテーションが付与されたSetterに対しては、
 * リクエストパラメータまたはURIから抽出されたパラメータがインジェクトされます。
 * </p>
 *
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Alias
public @interface RequestParameter {
    Populate z_alias() default @Populate(RequestParameterScope.class);

    String name() default "";

    String[] actionName() default {};
}
