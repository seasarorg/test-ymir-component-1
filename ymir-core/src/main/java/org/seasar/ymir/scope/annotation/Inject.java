package org.seasar.ymir.scope.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.annotation.Alias;
import org.seasar.ymir.annotation.ElementAlias;
import org.seasar.ymir.scope.impl.ComponentScope;

/**
 * Seasar2コンテナに登録されているコンポーネントをインジェクトすることを表すアノテーションです。
 * <p>このアノテーションが付与されたSetterに対しては、
 * Seasar2コンテナに登録されているコンポーネントがインジェクトされます。
 * </p>
 *
 * @author YOKOTA Takehiko
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.PARAMETER })
@Alias
public @interface Inject {
    In z_alias() default @In(ComponentScope.class);

    @ElementAlias("name")
    String value() default "";

    String[] actionName() default {};

    boolean required() default true;
}
