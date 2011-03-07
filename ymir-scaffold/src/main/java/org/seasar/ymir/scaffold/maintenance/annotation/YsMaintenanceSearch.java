package org.seasar.ymir.scaffold.maintenance.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface YsMaintenanceSearch {
    String[] excludeColumns() default {};

    YsMaintenanceSearchType[] type() default {};

    int recordsByPage() default 20;
}
