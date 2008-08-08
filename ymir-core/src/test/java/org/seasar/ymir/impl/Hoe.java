package org.seasar.ymir.impl;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Hoe {
    String value() default "";

    String value1() default "value1";

    String value2() default "value2";

    String value3() default "value3";
}
