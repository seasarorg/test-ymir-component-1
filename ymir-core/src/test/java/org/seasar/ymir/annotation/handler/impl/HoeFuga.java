package org.seasar.ymir.annotation.handler.impl;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.seasar.ymir.annotation.Collection;

@Retention(RetentionPolicy.RUNTIME)
@Collection
public @interface HoeFuga {
    Hoe[] abc() default { @Hoe(1), @Hoe(2) };

    Hoe[] zzz();

    Fuga value() default @Fuga;
}
