package org.seasar.ymir.annotation.handler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Hoe {
    String value();

    int abc();

    int zzz();
}
