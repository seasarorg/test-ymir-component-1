package org.seasar.ymir.dbflute.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.seasar.dbflute.Entity;
import org.seasar.dbflute.bhv.BehaviorWritable;
import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.cbean.ckey.ConditionKey;
import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.dbmeta.info.ColumnInfo;
import org.seasar.dbflute.dbmeta.info.UniqueInfo;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.dbflute.EntityManager;
import org.seasar.ymir.dbflute.util.DBFluteUtils;
import org.seasar.ymir.util.BeanUtils;
import org.seasar.ymir.util.ClassUtils;

public class EntityManagerImpl implements EntityManager {
    @Binding(bindingType = BindingType.MUST)
    protected ApplicationManager applicationManager;

    @Binding(bindingType = BindingType.MUST)
    protected TypeConversionManager typeConversionManager;

    private Map<String, Class<? extends Entity>> entityClassMap = new ConcurrentHashMap<String, Class<? extends Entity>>();

    private Map<Class<? extends Entity>, BehaviorWritable> behaviorMap = new ConcurrentHashMap<Class<? extends Entity>, BehaviorWritable>();

    private Map<Class<? extends Entity>, Class<? extends ConditionBean>> conditionBeanClassMap = new ConcurrentHashMap<Class<? extends Entity>, Class<? extends ConditionBean>>();

    private Map<Class<? extends Entity>, DBMeta> dbMetaMap = new ConcurrentHashMap<Class<? extends Entity>, DBMeta>();

    private Map<Class<? extends Entity>, List<String>> primaryKeyColumnNamesMap = new ConcurrentHashMap<Class<? extends Entity>, List<String>>();

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
        final String key = entityName;

        if (key.indexOf('_') >= 0) {
            // DBNameなのでエンティティ名に変換する。
            entityName = DBFluteUtils.camelize(key);
        } else if (key.length() > 0 && Character.isUpperCase(key.charAt(0))) {
            // 大文字で始まっていればDBNameとみなして小文字に変換する。
            entityName = key.toLowerCase();
        }

        BehaviorWritable bhv;
        try {
            bhv = (BehaviorWritable) applicationManager
                    .findContextApplication().getS2Container().getComponent(
                            entityName + "Bhv");
        } catch (ComponentNotFoundRuntimeException ex) {
            return null;
        }

        String bhvClassName = bhv.getClass().getName();
        String packageName = bhvClassName.substring(0, bhvClassName
                .lastIndexOf('.', bhvClassName.lastIndexOf('.') - 1));

        String cEntityName = BeanUtils.capitalize(entityName);
        try {
            entityClass = (Class<? extends Entity>) ClassUtils
                    .forName(packageName + ".exentity." + cEntityName);
        } catch (ClassNotFoundException ignore) {
            return null;
        }

        String cbClassName = packageName + ".cbean." + cEntityName + "CB";
        try {
            cbClass = (Class<ConditionBean>) ClassUtils.forName(cbClassName);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Cannot find ConditionBean class: "
                    + cbClassName);
        }

        entityClassMap.put(key, entityClass);
        behaviorMap.put(entityClass, bhv);
        conditionBeanClassMap.put(entityClass, cbClass);

        DBMeta dbMeta = newEntity(entityClass).getDBMeta();
        dbMetaMap.put(entityClass, dbMeta);

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
        DBMeta dbMeta = getDBMeta(entityClass);
        if (dbMeta == null) {
            return null;
        }

        try {
            return dbMeta.findColumnInfo(columnName);
        } catch (IllegalArgumentException ignore) {
            return null;
        }
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

    public void setValue(ConditionBean cb, String columnName, ConditionKey key,
            Object value) {
        if (value == null || value instanceof String
                && ((String) value).trim().length() == 0) {
            return;
        }

        ColumnInfo columnInfo = getColumnInfo(cb.getTableDbName(), columnName);
        if (columnInfo == null) {
            return;
        }

        cb.localCQ().invokeQuery(
                columnName,
                key.getConditionKey(),
                typeConversionManager.convert(value, columnInfo
                        .getPropertyType()));
    }
}
