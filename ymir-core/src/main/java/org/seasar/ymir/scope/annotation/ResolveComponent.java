package org.seasar.ymir.scope.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.annotation.Alias;
import org.seasar.ymir.annotation.ElementAlias;
import org.seasar.ymir.scope.impl.ComponentScope;
import org.seasar.ymir.scope.impl.RequestParameterScope;

/**
 * @since 1.0.3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Alias
public @interface ResolveComponent {
    Resolve z_alias() default @Resolve(ComponentScope.class);

    @ElementAlias("name")
    String value() default "";
}
