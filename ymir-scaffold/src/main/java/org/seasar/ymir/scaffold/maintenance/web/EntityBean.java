package org.seasar.ymir.scaffold.maintenance.web;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.dbflute.Entity;
import org.seasar.dbflute.bhv.BehaviorWritable;
import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.cbean.ckey.ConditionKey;
import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.dbmeta.info.ColumnInfo;
import org.seasar.ymir.Request;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.converter.PropertyHandler;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.dbflute.EntityManager;
import org.seasar.ymir.scaffold.maintenance.Constants;
import org.seasar.ymir.scaffold.maintenance.annotation.MaintenanceAdd;
import org.seasar.ymir.scaffold.maintenance.annotation.MaintenanceEdit;
import org.seasar.ymir.scaffold.maintenance.annotation.MaintenanceEntity;
import org.seasar.ymir.scaffold.maintenance.annotation.MaintenanceFK;
import org.seasar.ymir.scaffold.maintenance.annotation.MaintenanceIndex;
import org.seasar.ymir.scaffold.maintenance.dto.ColumnDto;
import org.seasar.ymir.scaffold.maintenance.enm.Action;

public class EntityBean implements Constants {
    private EntityManager entityManager;

    private TypeConversionManager typeConversionManager;

    private String entityName;

    private Class<? extends Entity> entityClass;

    private DBMeta dbMeta;

    private BehaviorWritable behavior;

    private List<String> primaryKeyColumnNames;

    private Set<String> readOnlyColumnNames = new HashSet<String>();

    private String createdDateColumnName;

    private String modifiedDateColumnName;

    private Set<String> passwordColumnNames = Collections.emptySet();

    private Map<String, ColumnDto> indexColumnMap = new LinkedHashMap<String, ColumnDto>();

    private Map<String, ColumnDto> addColumnMap = new LinkedHashMap<String, ColumnDto>();

    private Map<String, MaintenanceFK> fkMap = new HashMap<String, MaintenanceFK>();

    private Set<String> addUpdatableColumnNames = new HashSet<String>();

    private Map<String, ColumnDto> editColumnMap = new LinkedHashMap<String, ColumnDto>();

    private Set<String> editUpdatableColumnNames = new HashSet<String>();

    private Set<String> hiddenColumnNames = new LinkedHashSet<String>();

    private int recordsByPage = 20;

    public EntityBean(AnnotationHandler annotationHandler,
            EntityManager entityManager,
            TypeConversionManager typeConversionManager, String entityName,
            Class<?> pageClass) {
        this.entityManager = entityManager;
        this.typeConversionManager = typeConversionManager;
        this.entityName = entityName;

        entityClass = entityManager.getEntityClass(entityName);
        if (entityClass == null) {
            throw new IllegalArgumentException("Cannot find entity class for: "
                    + entityName);
        }

        dbMeta = entityManager.getDBMeta(entityClass);
        behavior = entityManager.getBehavior(entityClass);
        primaryKeyColumnNames = entityManager
                .getPrimaryKeyColumnNames(entityClass);

        readAnnotations(annotationHandler, pageClass);
    }

    private void readAnnotations(AnnotationHandler annotationHandler,
            Class<?> pageClass) {
        Set<String> indexColumnNames = new LinkedHashSet<String>();
        Set<String> indexExcludeColumnNames = new HashSet<String>();

        Set<String> addColumnNames = new LinkedHashSet<String>();

        Set<String> editColumnNames = new LinkedHashSet<String>();
        Set<String> editExcludeColumnNames = new HashSet<String>();

        readOnlyColumnNames = new HashSet<String>();

        createdDateColumnName = DEFAULT_COLUMN_CREATED_DATE;
        modifiedDateColumnName = DEFAULT_COLUMN_MODIFIED_DATE;
        MaintenanceEntity entityAnn = annotationHandler.getAnnotation(
                pageClass, MaintenanceEntity.class);
        if (entityAnn != null) {
            createdDateColumnName = entityAnn.createdDateColumn();
            if (createdDateColumnName.length() == 0) {
                createdDateColumnName = null;
            }
            modifiedDateColumnName = entityAnn.modifiedDateColumn();
            if (modifiedDateColumnName.length() == 0) {
                modifiedDateColumnName = null;
            }
            passwordColumnNames = new HashSet<String>(Arrays.asList(entityAnn
                    .passwordColumn()));
            indexExcludeColumnNames.addAll(passwordColumnNames);
        }

        for (MaintenanceFK ann : annotationHandler.getAnnotations(pageClass,
                MaintenanceFK.class)) {
            fkMap.put(ann.column(), ann);
        }

        MaintenanceIndex indexAnn = annotationHandler.getAnnotation(pageClass,
                MaintenanceIndex.class);
        if (indexAnn != null) {
            indexColumnNames.addAll(Arrays.asList(indexAnn.columnsOrder()));
            indexExcludeColumnNames.addAll(Arrays.asList(indexAnn
                    .excludeColumns()));
            recordsByPage = indexAnn.recordsByPage();
        }

        MaintenanceAdd addAnn = annotationHandler.getAnnotation(pageClass,
                MaintenanceAdd.class);
        if (addAnn != null) {
            addColumnNames.addAll(Arrays.asList(addAnn.columnsOrder()));
        }

        MaintenanceEdit editAnn = annotationHandler.getAnnotation(pageClass,
                MaintenanceEdit.class);
        if (editAnn != null) {
            editColumnNames.addAll(Arrays.asList(editAnn.columnsOrder()));
            editExcludeColumnNames.addAll(Arrays.asList(editAnn
                    .excludeColumns()));
            readOnlyColumnNames
                    .addAll(Arrays.asList(editAnn.readOnlyColumns()));
        }

        Map<String, ColumnInfo> columnInfoMap = new HashMap<String, ColumnInfo>();
        for (ColumnInfo columnInfo : dbMeta.getColumnInfoList()) {
            String name = columnInfo.getPropertyName();
            columnInfoMap.put(name, columnInfo);

            if (!COLUMN_VERSION_NO.equals(name)) {
                indexColumnNames.add(name);
                if (!columnInfo.isPrimary()) {
                    addColumnNames.add(name);
                }
                editColumnNames.add(name);
            }

            if (columnInfo.isPrimary()) {
                readOnlyColumnNames.add(name);
                hiddenColumnNames.add(name);
            }
        }

        addColumnNames.remove(createdDateColumnName);
        editColumnNames.remove(createdDateColumnName);

        addColumnNames.remove(modifiedDateColumnName);
        editColumnNames.remove(modifiedDateColumnName);

        for (String name : indexColumnNames) {
            if (!indexExcludeColumnNames.contains(name)) {
                indexColumnMap.put(name, new ColumnDto(columnInfoMap.get(name),
                        getFK(name)));
            }
        }

        for (String name : addColumnNames) {
            addColumnMap.put(name, new ColumnDto(columnInfoMap.get(name)));
            if (!isPasswordColumn(name)) {
                addUpdatableColumnNames.add(name);
            }
        }

        for (String name : editColumnNames) {
            if (!editExcludeColumnNames.contains(name)) {
                boolean readOnly = readOnlyColumnNames.contains(name);
                editColumnMap.put(name, new ColumnDto(columnInfoMap.get(name),
                        readOnly));
                if (!readOnly && !isPasswordColumn(name)) {
                    editUpdatableColumnNames.add(name);
                }
            }
        }
    }

    public Class<? extends Entity> getEntityClass() {
        return entityClass;
    }

    public List<String> getPrimaryKeyColumnNames() {
        return primaryKeyColumnNames;
    }

    public String getEntityName() {
        return entityName;
    }

    public ConditionBean newConditionBean() {
        return entityManager.newConditionBean(entityClass);
    }

    public BehaviorWritable getBehavior() {
        return behavior;
    }

    public Entity newEntity() {
        return entityManager.newEntity(entityClass);
    }

    public Entity loadEntity(Request request) {
        ConditionBean cb = newConditionBean();
        for (String name : getPrimaryKeyColumnNames()) {
            entityManager.setValue(cb, name, ConditionKey.CK_EQUAL, request
                    .getParameter(name));
        }
        return behavior.readEntityWithDeletedCheck(cb);
    }

    public Map<String, ColumnDto> getColumnMap(Action action) {
        switch (action) {
        case INDEX:
            return indexColumnMap;

        case ADD:
            return addColumnMap;

        case EDIT:
            return editColumnMap;

        default:
            throw new IllegalArgumentException("Illegal action: "
                    + action.getName());
        }
    }

    public Set<String> getUpdatableColumnNames(Action action) {
        switch (action) {
        case ADD:
            return addUpdatableColumnNames;

        case EDIT:
            return editUpdatableColumnNames;

        default:
            throw new IllegalArgumentException("Illegal action: "
                    + action.getName());
        }
    }

    public Set<String> getHiddenColumnNames(Action action) {
        if (action == Action.EDIT) {
            return hiddenColumnNames;
        } else {
            return Collections.emptySet();
        }
    }

    public String getCreatedDateColumnName() {
        return createdDateColumnName;
    }

    public String getModifiedDateColumnName() {
        return modifiedDateColumnName;
    }

    public String getColumnValueAsString(Entity entity, String columnName) {
        PropertyHandler handler = typeConversionManager.getPropertyHandler(
                entity, columnName);
        if (handler == null) {
            return null;
        }
        try {
            Object value = handler.getProperty();
            if (value != null) {
                return value.toString();
            } else {
                return null;
            }
        } catch (IllegalAccessException ex) {
            return null;
        } catch (NoSuchMethodException ex) {
            return null;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public int getRecordsByPage() {
        return recordsByPage;
    }

    public ColumnInfo getColumnInfo(String columnName) {
        return entityManager.getColumnInfo(entityClass, columnName);
    }

    public boolean isPasswordColumn(String columnName) {
        return passwordColumnNames.contains(columnName);
    }

    public Set<String> getPasswordColumnNames() {
        return passwordColumnNames;
    }

    public boolean isIncludedColumn(Action action, String columnName) {
        return getColumnMap(action).containsKey(columnName);
    }

    public boolean isReadOnlyColumn(String columnName) {
        return readOnlyColumnNames.contains(columnName);
    }

    public MaintenanceFK getFK(String columnName) {
        return fkMap.get(columnName);
    }

    public Set<String> getOuterColumns() {
        return fkMap.keySet();
    }
}
