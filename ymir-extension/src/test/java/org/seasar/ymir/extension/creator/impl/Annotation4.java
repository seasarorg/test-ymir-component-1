package org.seasar.ymir.extension.creator.impl;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Annotation4 {
    String[] value() default "";
}
