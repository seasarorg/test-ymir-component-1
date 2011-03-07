package org.seasar.ymir.scaffold.maintenance.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.dbflute.Entity;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface YsMaintenanceFk {
    String column();

    Class<? extends Entity> foreignEntity();

    String foreignColumn();
}
