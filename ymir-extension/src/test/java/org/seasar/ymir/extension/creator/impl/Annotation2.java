package org.seasar.ymir.extension.creator.impl;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Annotation2 {
    Annotation3 annotation();
}
