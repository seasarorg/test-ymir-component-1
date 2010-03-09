package org.seasar.ymir.dbflute;

import java.util.List;

import org.seasar.dbflute.Entity;
import org.seasar.dbflute.bhv.BehaviorWritable;
import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.dbmeta.info.ColumnInfo;

public interface EntityManager {
    Class<? extends Entity> getEntityClass(String entityName);

    Entity newEntity(String entityName);

    Entity newEntity(Class<? extends Entity> entityClass);

    BehaviorWritable getBehavior(String entityName);

    BehaviorWritable getBehavior(Class<? extends Entity> entityClass);

    ConditionBean newConditionBean(String entityName);

    ConditionBean newConditionBean(Class<? extends Entity> entityClass);

    DBMeta getDBMeta(String entityName);

    DBMeta getDBMeta(Class<? extends Entity> entityClass);

    ColumnInfo getColumnInfo(String entityName, String columnName);

    ColumnInfo getColumnInfo(Class<? extends Entity> entityClass,
            String columnName);

    List<String> getPrimaryKeyColumnNames(String entityName);

    List<String> getPrimaryKeyColumnNames(Class<? extends Entity> entityClass);
}
