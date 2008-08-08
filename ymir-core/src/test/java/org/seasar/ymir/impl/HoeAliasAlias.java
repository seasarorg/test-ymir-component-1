package org.seasar.ymir.impl;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.seasar.ymir.annotation.Alias;

@Retention(RetentionPolicy.RUNTIME)
@Alias
public @interface HoeAliasAlias {
    HoeAlias z_alias() default @HoeAlias;

    String value() default "";
}
