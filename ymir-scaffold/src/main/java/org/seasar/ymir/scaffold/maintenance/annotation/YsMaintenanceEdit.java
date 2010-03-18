package org.seasar.ymir.scaffold.maintenance.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface YsMaintenanceEdit {
    String[] columnsOrder() default {};

    String[] excludeColumns() default {};

    String[] readOnlyColumns() default {};
}
