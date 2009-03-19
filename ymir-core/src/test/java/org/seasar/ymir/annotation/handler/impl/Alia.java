package org.seasar.ymir.annotation.handler.impl;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.seasar.ymir.annotation.Alias;
import org.seasar.ymir.annotation.ElementAlias;
import org.seasar.ymir.impl.Hoe;

@Retention(RetentionPolicy.RUNTIME)
@Alias
public @interface Alia {
    Orig z_alias() default @Orig;

    @ElementAlias("name2")
    String name1() default "alia1";

    String name4() default "alia4";

    @ElementAlias("name3")
    String name5() default "alia5";
}
