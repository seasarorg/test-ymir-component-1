package org.seasar.ymir.scaffold.maintenance.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.seasar.ymir.scaffold.maintenance.enm.SearchType;

@Retention(RetentionPolicy.RUNTIME)
public @interface YsMaintenanceSearchType {
    String column();

    SearchType type();
}
