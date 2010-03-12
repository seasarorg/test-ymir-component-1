package org.seasar.ymir.scaffold.maintenance.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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

    private String createdDateColumnName;

    private String modifiedDateColumnName;

    private List<ColumnDto> indexColumns = new ArrayList<ColumnDto>();

    private List<ColumnDto> addColumns = new ArrayList<ColumnDto>();

    private List<String> addUpdatableColumnNames = new ArrayList<String>();

    private List<ColumnDto> editColumns = new ArrayList<ColumnDto>();

    private List<String> editUpdatableColumnNames = new ArrayList<String>();

    private List<String> hiddenColumnNames = new ArrayList<String>();

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
            throw new RuntimeException("Cannot find entity class for: "
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
        }

        Set<String> indexColumnNames = new LinkedHashSet<String>();
        Set<String> indexExcludeColumnNames = new HashSet<String>();
        MaintenanceIndex indexAnn = annotationHandler.getAnnotation(pageClass,
                MaintenanceIndex.class);
        if (indexAnn != null) {
            indexColumnNames.addAll(Arrays.asList(indexAnn.columnsOrder()));
            indexExcludeColumnNames.addAll(Arrays.asList(indexAnn
                    .excludeColumns()));
            recordsByPage = indexAnn.recordsByPage();
        }

        Set<String> addColumnNames = new LinkedHashSet<String>();
        MaintenanceAdd addAnn = annotationHandler.getAnnotation(pageClass,
                MaintenanceAdd.class);
        if (addAnn != null) {
            addColumnNames.addAll(Arrays.asList(addAnn.columnsOrder()));
        }

        Set<String> editColumnNames = new LinkedHashSet<String>();
        Set<String> editExcludeColumnNames = new HashSet<String>();
        Set<String> readOnlyColumnNames = new HashSet<String>();
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
                indexColumns.add(new ColumnDto(columnInfoMap.get(name)));
            }
        }

        for (String name : addColumnNames) {
            addColumns.add(new ColumnDto(columnInfoMap.get(name)));
            addUpdatableColumnNames.add(name);
        }

        for (String name : editColumnNames) {
            if (!editExcludeColumnNames.contains(name)) {
                boolean readOnly = readOnlyColumnNames.contains(name);
                editColumns
                        .add(new ColumnDto(columnInfoMap.get(name), readOnly));
                if (!readOnly) {
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

    public List<ColumnDto> getColumns(Action action) {
        switch (action) {
        case INDEX:
            return indexColumns;

        case ADD:
            return addColumns;

        case EDIT:
            return editColumns;

        default:
            throw new IllegalArgumentException("Illegal action: "
                    + action.getName());
        }
    }

    public List<String> getUpdatableColumnNames(Action action) {
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

    public List<String> getHiddenColumnNames(Action action) {
        if (action == Action.EDIT) {
            return hiddenColumnNames;
        } else {
            return Collections.emptyList();
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
}
