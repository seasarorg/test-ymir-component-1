package org.seasar.ymir.scaffold.maintenance.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.annotation.Collection;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Collection
public @interface MaintenanceFKs {
    MaintenanceFK[] value();
}
