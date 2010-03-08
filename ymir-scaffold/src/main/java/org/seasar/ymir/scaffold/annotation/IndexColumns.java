package org.seasar.ymir.scaffold.annotation;

public @interface IndexColumns {
    String[] includes() default {};

    String[] excludes() default {};
}
