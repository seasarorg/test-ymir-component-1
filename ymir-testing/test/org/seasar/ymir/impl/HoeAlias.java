package org.seasar.ymir.impl;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.seasar.ymir.annotation.Alias;

@Retention(RetentionPolicy.RUNTIME)
@Alias
public @interface HoeAlias {
    Hoe z_alias() default @Hoe(value1 = "value1_overwritten");

    String value() default "";

    String value2() default "value2_overwritten";
}
