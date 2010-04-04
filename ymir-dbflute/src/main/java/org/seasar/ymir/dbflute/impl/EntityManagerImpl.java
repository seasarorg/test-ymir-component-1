package org.seasar.ymir.dbflute.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.dbflute.Entity;
import org.seasar.dbflute.bhv.BehaviorWritable;
import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.cbean.ckey.ConditionKey;
import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.dbmeta.info.ColumnInfo;
import org.seasar.dbflute.dbmeta.info.ForeignInfo;
import org.seasar.dbflute.dbmeta.info.UniqueInfo;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.InitMethod;
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

    private Map<String, Class<? extends Entity>> entityClassMap;

    private Map<Class<? extends Entity>, BehaviorWritable> behaviorMap;

    private Map<Class<? extends Entity>, Class<? extends ConditionBean>> conditionBeanClassMap;

    private Map<Class<? extends Entity>, DBMeta> dbMetaMap;

    private Map<Class<? extends Entity>, List<String>> primaryKeyColumnNamesMap;

    private Map<Class<? extends Entity>, List<ForeignInfo>> childForeignInfoMap;

    @SuppressWarnings("unchecked")
    @InitMethod
    public void initialize() {
        entityClassMap = new HashMap<String, Class<? extends Entity>>();
        behaviorMap = new HashMap<Class<? extends Entity>, BehaviorWritable>();
        conditionBeanClassMap = new HashMap<Class<? extends Entity>, Class<? extends ConditionBean>>();
        dbMetaMap = new HashMap<Class<? extends Entity>, DBMeta>();
        primaryKeyColumnNamesMap = new HashMap<Class<? extends Entity>, List<String>>();
        childForeignInfoMap = new HashMap<Class<? extends Entity>, List<ForeignInfo>>();

        for (BehaviorWritable bhv : (BehaviorWritable[]) applicationManager
                .findContextApplication().getS2Container().findComponents(
                        BehaviorWritable.class)) {
            String bhvClassName = bhv.getClass().getName();
            String packageName = bhvClassName.substring(0, bhvClassName
                    .lastIndexOf('.', bhvClassName.lastIndexOf('.') - 1));
            DBMeta dbMeta = bhv.getDBMeta();
            String entityName = dbMeta.getTablePropertyName();
            if (entityClassMap.containsKey(entityName)) {
                // TODO YS_USERなどの重複登録問題が解決するまではこうしておく。
                continue;
            }

            Class<? extends Entity> entityClass;
            String cEntityName = BeanUtils.capitalize(entityName);
            String entityClassName = packageName + ".exentity." + cEntityName;
            try {
                entityClass = (Class<? extends Entity>) ClassUtils
                        .forName(entityClassName);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException("Entity class corresponding to '"
                        + bhvClassName + "' not found: " + entityClassName, ex);
            }

            Class<? extends ConditionBean> cbClass;
            String cbClassName = packageName + ".cbean." + cEntityName + "CB";
            try {
                cbClass = (Class<? extends ConditionBean>) ClassUtils
                        .forName(cbClassName);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException("Cannot find ConditionBean class: "
                        + cbClassName);
            }

            entityClassMap.put(entityName, entityClass);
            dbMetaMap.put(entityClass, dbMeta);
            behaviorMap.put(entityClass, bhv);
            conditionBeanClassMap.put(entityClass, cbClass);

            List<String> primaryKeyColumnNames = new ArrayList<String>();
            UniqueInfo uniqueInfo = dbMeta.getPrimaryUniqueInfo();
            for (ColumnInfo columnInfo : uniqueInfo.getUniqueColumnList()) {
                primaryKeyColumnNames.add(columnInfo.getPropertyName());
            }
            primaryKeyColumnNamesMap.put(entityClass, Collections
                    .unmodifiableList(primaryKeyColumnNames));
        }

        for (DBMeta dbMeta : dbMetaMap.values()) {
            for (ForeignInfo info : dbMeta.getForeignInfoList()) {
                Class<? extends Entity> foreignEntityClass = entityClassMap
                        .get(info.getForeignDBMeta().getTablePropertyName());
                List<ForeignInfo> list = childForeignInfoMap
                        .get(foreignEntityClass);
                if (list == null) {
                    list = new ArrayList<ForeignInfo>();
                    childForeignInfoMap.put(foreignEntityClass, list);
                }
                list.add(info);
            }
        }

        entityClassMap = Collections.unmodifiableMap(entityClassMap);
        behaviorMap = Collections.unmodifiableMap(behaviorMap);
        conditionBeanClassMap = Collections
                .unmodifiableMap(conditionBeanClassMap);
        dbMetaMap = Collections.unmodifiableMap(dbMetaMap);
        primaryKeyColumnNamesMap = Collections
                .unmodifiableMap(primaryKeyColumnNamesMap);
        childForeignInfoMap = Collections.unmodifiableMap(childForeignInfoMap);
    }

    public Class<? extends Entity> getEntityClass(String entityName) {
        if (entityName == null) {
            return null;
        }

        return entityClassMap.get(normalizeEntityName(entityName));
    }

    String normalizeEntityName(String entityName) {
        if (entityName == null) {
            return null;
        }

        if (entityName.indexOf('_') >= 0) {
            // DBNameなのでエンティティ名に変換する。
            return DBFluteUtils.camelize(entityName);
        } else if (entityName.length() > 0
                && Character.isUpperCase(entityName.charAt(0))) {
            // 大文字で始まっていればDBNameとみなして小文字に変換する。
            return entityName.toLowerCase();
        }
        return entityName;
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
        List<String> primaryKeyColumnNames = null;
        if (entityClass != null) {
            primaryKeyColumnNames = primaryKeyColumnNamesMap.get(entityClass);
        }
        if (primaryKeyColumnNames == null) {
            primaryKeyColumnNames = Collections.emptyList();
        }
        return primaryKeyColumnNames;
    }

    public List<ForeignInfo> getChildForeignInfos(String entityName) {
        return getChildForeignInfos(getEntityClass(entityName));
    }

    public List<ForeignInfo> getChildForeignInfos(
            Class<? extends Entity> entityClass) {
        List<ForeignInfo> childForeignInfos = null;
        if (entityClass != null) {
            childForeignInfos = childForeignInfoMap.get(entityClass);
        }
        if (childForeignInfos == null) {
            childForeignInfos = Collections.emptyList();
        }
        return childForeignInfos;
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
