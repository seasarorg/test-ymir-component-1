package org.seasar.ymir.impl;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.seasar.ymir.annotation.Collection;

@Retention(RetentionPolicy.RUNTIME)
@Collection
public @interface HoeFuga {
    Hoe[] value() default { @Hoe, @Hoe };

    Fuga value2() default @Fuga;
}
