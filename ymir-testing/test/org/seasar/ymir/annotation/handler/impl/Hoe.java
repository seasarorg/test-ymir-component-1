package org.seasar.ymir.annotation.handler.impl;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Hoe {
    int value() default 0;
}
