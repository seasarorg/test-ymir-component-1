package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Out {
    Class value() default Object.class;

    String name() default "";

    Class scopeClass() default Object.class;

    String scopeName() default "";

    String[] actionName() default {};

    boolean outjectWhereNull() default true;
}
