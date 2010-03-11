package org.seasar.ymir.scaffold.maintenance.annotation;

import static org.seasar.ymir.scaffold.maintenance.Constants.DEFAULT_COLUMN_CREATED_DATE;
import static org.seasar.ymir.scaffold.maintenance.Constants.DEFAULT_COLUMN_MODIFIED_DATE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MaintenanceEntity {
    String createdDateColumn() default DEFAULT_COLUMN_CREATED_DATE;

    String modifiedDateColumn() default DEFAULT_COLUMN_MODIFIED_DATE;
}
