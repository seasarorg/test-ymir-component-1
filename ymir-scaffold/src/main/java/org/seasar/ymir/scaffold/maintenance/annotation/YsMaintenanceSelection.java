package org.seasar.ymir.scaffold.maintenance.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.scaffold.maintenance.enm.SelectionType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface YsMaintenanceSelection {
    String column();

    SelectionType type();

    YsMaintenanceEnum[] enums() default {};
}
