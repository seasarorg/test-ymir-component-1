package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.scope.impl.MapScope;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Alias
public @interface MapParameter {
    Populate z_alias() default @Populate(MapScope.class);

    String[] actionName() default {};

    boolean populateWhereNull() default false;
}
