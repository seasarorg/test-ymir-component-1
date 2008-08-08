package org.seasar.ymir.impl;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.seasar.ymir.annotation.Alias;
import org.seasar.ymir.constraint.annotation.Required;

@Retention(RetentionPolicy.RUNTIME)
@Alias
public @interface RequiredAlias {
    Required z_alias() default @Required;
}
