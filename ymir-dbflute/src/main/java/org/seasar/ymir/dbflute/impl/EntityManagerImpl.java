package org.seasar.ymir.dbflute.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.seasar.dbflute.Entity;
import org.seasar.dbflute.bhv.BehaviorWritable;
import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.dbmeta.info.ColumnInfo;
import org.seasar.dbflute.dbmeta.info.UniqueInfo;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.convention.YmirNamingConvention;
import org.seasar.ymir.dbflute.EntityManager;
import org.seasar.ymir.util.ClassUtils;

public class EntityManagerImpl implements EntityManager {
    private Map<String, Class<? extends Entity>> entityClassMap = new ConcurrentHashMap<String, Class<? extends Entity>>();

    private Map<Class<? extends Entity>, BehaviorWritable> behaviorMap = new ConcurrentHashMap<Class<? extends Entity>, BehaviorWritable>();

    private Map<Class<? extends Entity>, Class<? extends ConditionBean>> conditionBeanClassMap = new ConcurrentHashMap<Class<? extends Entity>, Class<? extends ConditionBean>>();

    private Map<Class<? extends Entity>, DBMeta> dbMetaMap = new ConcurrentHashMap<Class<? extends Entity>, DBMeta>();

    private Map<Class<? extends Entity>, Map<String, ColumnInfo>> columnInfoMap = new ConcurrentHashMap<Class<? extends Entity>, Map<String, ColumnInfo>>();

    private Map<Class<? extends Entity>, List<String>> primaryKeyColumnNamesMap = new ConcurrentHashMap<Class<? extends Entity>, List<String>>();

    @Binding(bindingType = BindingType.MUST)
    protected ApplicationManager applicationManager;

    @Binding(bindingType = BindingType.MUST)
    protected YmirNamingConvention ymirNamingConvention;

    public Class<? extends Entity> getEntityClass(String entityName) {
        if (entityName == null) {
            return null;
        }

        Class<? extends Entity> entityClass = entityClassMap.get(entityName);
        if (entityClass == null) {
            entityClass = prepareForEntity(entityName);
        }

        return entityClass;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Entity> prepareForEntity(String entityName) {
        Class<? extends Entity> entityClass = null;
        Class<ConditionBean> cbClass = null;

        for (String rootPackageName : ymirNamingConvention
                .getRootPackageNames()) {
            try {
                entityClass = (Class<? extends Entity>) ClassUtils
                        .forName(rootPackageName + ".dbflute.exentity."
                                + entityName);
            } catch (ClassNotFoundException ignore) {
                continue;
            }

            String cbClassName = rootPackageName + ".dbflute.cbean."
                    + entityName + "CB";
            try {
                cbClass = (Class<ConditionBean>) ClassUtils
                        .forName(cbClassName);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException("Cannot find ConditionBean class: "
                        + cbClassName);
            }
        }
        if (entityClass == null) {
            return null;
        }

        entityClassMap.put(entityName, entityClass);
        behaviorMap.put(entityClass, (BehaviorWritable) applicationManager
                .findContextApplication().getS2Container().getComponent(
                        entityName + "Bhv"));
        conditionBeanClassMap.put(entityClass, cbClass);

        DBMeta dbMeta = newEntity(entityClass).getDBMeta();
        dbMetaMap.put(entityClass, dbMeta);

        Map<String, ColumnInfo> ciMap = new HashMap<String, ColumnInfo>();
        for (ColumnInfo columnInfo : dbMeta.getColumnInfoList()) {
            ciMap.put(columnInfo.getPropertyName(), columnInfo);
        }
        columnInfoMap.put(entityClass, ciMap);

        List<String> primaryKeyColumnNames = new ArrayList<String>();
        UniqueInfo uniqueInfo = dbMeta.getPrimaryUniqueInfo();
        for (ColumnInfo columnInfo : uniqueInfo.getUniqueColumnList()) {
            primaryKeyColumnNames.add(columnInfo.getPropertyName());
        }
        primaryKeyColumnNamesMap.put(entityClass, Collections
                .unmodifiableList(primaryKeyColumnNames));

        return entityClass;
    }

    public Entity newEntity(String entityName) {
        return newEntity(getEntityClass(entityName));
    }

    public Entity newEntity(Class<? extends Entity> entityClass) {
        if (entityClass == null) {
            return null;
        }

        try {
            return entityClass.newInstance();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public BehaviorWritable getBehavior(String entityName) {
        return getBehavior(getEntityClass(entityName));
    }

    public BehaviorWritable getBehavior(Class<? extends Entity> entityClass) {
        if (entityClass == null) {
            return null;
        }

        return behaviorMap.get(entityClass);
    }

    public ConditionBean newConditionBean(String entityName) {
        return newConditionBean(getEntityClass(entityName));
    }

    public ConditionBean newConditionBean(Class<? extends Entity> entityClass) {
        if (entityClass == null) {
            return null;
        }

        Class<? extends ConditionBean> cbClass = conditionBeanClassMap
                .get(entityClass);
        if (cbClass == null) {
            return null;
        }

        try {
            return cbClass.newInstance();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public DBMeta getDBMeta(String entityName) {
        return getDBMeta(getEntityClass(entityName));
    }

    public DBMeta getDBMeta(Class<? extends Entity> entityClass) {
        if (entityClass == null) {
            return null;
        }

        return dbMetaMap.get(entityClass);
    }

    public ColumnInfo getColumnInfo(String entityName, String columnName) {
        return getColumnInfo(getEntityClass(entityName), columnName);
    }

    public ColumnInfo getColumnInfo(Class<? extends Entity> entityClass,
            String columnName) {
        if (entityClass == null) {
            return null;
        }
        Map<String, ColumnInfo> map = columnInfoMap.get(entityClass);
        if (map == null) {
            return null;
        }

        return map.get(columnName);
    }

    public List<String> getPrimaryKeyColumnNames(String entityName) {
        return getPrimaryKeyColumnNames(getEntityClass(entityName));
    }

    public List<String> getPrimaryKeyColumnNames(
            Class<? extends Entity> entityClass) {
        if (entityClass == null) {
            return null;
        }

        return primaryKeyColumnNamesMap.get(entityClass);
    }
}