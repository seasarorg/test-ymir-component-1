package org.seasar.ymir.annotation.handler.impl;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Orig {
    String name1() default "orig1";

    String name2() default "orig2";

    String name3() default "orig3";

    String name4() default "orig4";
}
