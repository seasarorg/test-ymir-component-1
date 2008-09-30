package org.seasar.ymir.annotation.handler.impl;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Fuga {
    int value() default 0;
}
