package org.seasar.ymir.scaffold.maintenance.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface YsMaintenanceEntity {
    String labelColumn() default "";

    String[] passwordColumns() default {};

    YsMaintenanceSelection[] selectionColumns() default {};

    YsMaintenanceFk[] hasOne() default {};

    YsMaintenanceFk[] hasMany() default {};

    YsMaintenanceFk[] belongsTo() default {};

    String[] columnsOrder() default {};

    String[] excludeColumns() default {};

    String[] readOnlyColumns() default {};
}
