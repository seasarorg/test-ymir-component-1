package org.seasar.ymir.scaffold.maintenance.web;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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
import org.seasar.dbflute.dbmeta.info.ForeignInfo;
import org.seasar.ymir.Request;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.converter.PropertyHandler;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.dbflute.EntityManager;
import org.seasar.ymir.scaffold.SiteManager;
import org.seasar.ymir.scaffold.maintenance.Globals;
import org.seasar.ymir.scaffold.maintenance.annotation.YsMaintenanceEntity;
import org.seasar.ymir.scaffold.maintenance.annotation.YsMaintenanceFk;
import org.seasar.ymir.scaffold.maintenance.annotation.YsMaintenanceSearch;
import org.seasar.ymir.scaffold.maintenance.annotation.YsMaintenanceSearchType;
import org.seasar.ymir.scaffold.maintenance.annotation.YsMaintenanceSelection;
import org.seasar.ymir.scaffold.maintenance.dto.ColumnDto;
import org.seasar.ymir.scaffold.maintenance.enm.Pane;
import org.seasar.ymir.scaffold.maintenance.enm.RelationType;
import org.seasar.ymir.scaffold.maintenance.enm.SearchType;

public class EntityBean implements Globals {
    private EntityManager entityManager;

    private TypeConversionManager typeConversionManager;

    private String entityName;

    private Class<? extends Entity> entityClass;

    private DBMeta dbMeta;

    private BehaviorWritable behavior;

    private List<String> primaryKeyColumnNames;

    private Map<String, SearchType> searchTypeMap = new HashMap<String, SearchType>();

    private Set<String> passwordColumnNames = new LinkedHashSet<String>();

    private Set<String> hiddenColumnNames = new LinkedHashSet<String>();

    private Set<String> readOnlyColumnNames = new LinkedHashSet<String>();

    private Set<String> updatableColumnNames = new LinkedHashSet<String>();

    private Map<String, ColumnDto> searchColumnMap = new LinkedHashMap<String, ColumnDto>();

    private Map<String, ColumnDto> listColumnMap = new LinkedHashMap<String, ColumnDto>();

    private Map<String, ColumnDto> detailColumnMap = new LinkedHashMap<String, ColumnDto>();

    private Map<String, ColumnDto> addColumnMap = new LinkedHashMap<String, ColumnDto>();

    private Map<String, ColumnDto> editColumnMap = new LinkedHashMap<String, ColumnDto>();

    private String labelColumnName;

    private int recordsByPage = 20;

    private Map<String, YsMaintenanceSelection> selectionColumnMap = new LinkedHashMap<String, YsMaintenanceSelection>();

    private Map<String, YsMaintenanceFk> hasOneFkMap = new LinkedHashMap<String, YsMaintenanceFk>();

    private Map<String, YsMaintenanceFk> hasManyFkMap = new LinkedHashMap<String, YsMaintenanceFk>();

    private Map<String, YsMaintenanceFk> belongsToFkMap = new LinkedHashMap<String, YsMaintenanceFk>();

    private Map<String, YsMaintenanceFk> fkMap = new HashMap<String, YsMaintenanceFk>();

    private Map<String, RelationType> relationTypeMap = new HashMap<String, RelationType>();

    public EntityBean(AnnotationHandler annotationHandler,
            EntityManager entityManager, SiteManager siteManager,
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

        readAnnotations(annotationHandler, entityClass, pageClass);
    }

    private void readAnnotations(AnnotationHandler annotationHandler,
            Class<?> entityClass, Class<?> pageClass) {
        Set<String> searchColumnNames = new LinkedHashSet<String>();
        Set<String> indexColumnNames = new LinkedHashSet<String>();
        Set<String> viewColumnNames = new LinkedHashSet<String>();
        Set<String> addColumnNames = new LinkedHashSet<String>();
        Set<String> editColumnNames = new LinkedHashSet<String>();

        YsMaintenanceEntity entityAnn = annotationHandler.getAnnotation(
                entityClass, YsMaintenanceEntity.class);
        if (entityAnn != null) {
            List<String> columnsOrder = Arrays.asList(entityAnn.columnsOrder());
            searchColumnNames.addAll(columnsOrder);
            indexColumnNames.addAll(columnsOrder);
            viewColumnNames.addAll(columnsOrder);
            addColumnNames.addAll(columnsOrder);
            editColumnNames.addAll(columnsOrder);
        }

        Map<String, ColumnInfo> columnInfoMap = new HashMap<String, ColumnInfo>();
        for (ColumnInfo columnInfo : dbMeta.getColumnInfoList()) {
            String name = columnInfo.getPropertyName();
            columnInfoMap.put(name, columnInfo);

            searchColumnNames.add(name);
            indexColumnNames.add(name);
            viewColumnNames.add(name);
            editColumnNames.add(name);
            addColumnNames.add(name);

            if (columnInfo.isPrimary()) {
                readOnlyColumnNames.add(name);
                hiddenColumnNames.add(name);
                addColumnNames.remove(name);
            }
            if (COLUMN_VERSION_NO.equals(name)) {
                readOnlyColumnNames.add(name);
                hiddenColumnNames.add(name);
                addColumnNames.remove(name);
                editColumnNames.remove(name);
            }
        }

        if (entityAnn != null) {
            labelColumnName = entityAnn.labelColumn().length() > 0 ? entityAnn
                    .labelColumn() : null;

            List<String> passwordColumns = Arrays.asList(entityAnn
                    .passwordColumns());
            List<String> readOnlyColumns = Arrays.asList(entityAnn
                    .readOnlyColumns());
            List<String> excludeColumns = Arrays.asList(entityAnn
                    .excludeColumns());

            passwordColumnNames.addAll(passwordColumns);
            readOnlyColumnNames.addAll(readOnlyColumns);

            searchColumnNames.removeAll(excludeColumns);
            indexColumnNames.removeAll(excludeColumns);
            viewColumnNames.removeAll(excludeColumns);
            addColumnNames.removeAll(excludeColumns);
            editColumnNames.removeAll(excludeColumns);

            for (YsMaintenanceSelection fk : entityAnn.selectionColumns()) {
                selectionColumnMap.put(fk.column(), fk);
            }
            for (YsMaintenanceFk fk : entityAnn.hasOne()) {
                fkMap.put(fk.column(), fk);
                hasOneFkMap.put(fk.column(), fk);
            }
            for (YsMaintenanceFk fk : entityAnn.hasMany()) {
                fkMap.put(fk.column(), fk);
                hasManyFkMap.put(fk.column(), fk);
            }
            for (YsMaintenanceFk fk : entityAnn.belongsTo()) {
                fkMap.put(fk.column(), fk);
                belongsToFkMap.put(fk.column(), fk);
            }
        }

        YsMaintenanceSearch searchAnn = annotationHandler.getAnnotation(
                pageClass, YsMaintenanceSearch.class);
        if (searchAnn != null) {
            searchColumnNames.removeAll(Arrays.asList(searchAnn
                    .excludeColumns()));
            for (YsMaintenanceSearchType searchType : searchAnn.type()) {
                searchTypeMap.put(searchType.column(), searchType.type());
            }
            recordsByPage = searchAnn.recordsByPage();
        }

        for (String name : searchColumnNames) {
            searchColumnMap.put(name,
                    new ColumnDto(entityManager, this, columnInfoMap.get(name),
                            getFk(name), getRelationType(name)));
        }

        for (String name : indexColumnNames) {
            listColumnMap.put(name,
                    new ColumnDto(entityManager, this, columnInfoMap.get(name),
                            getFk(name), getRelationType(name)));
        }

        for (String name : viewColumnNames) {
            detailColumnMap.put(name,
                    new ColumnDto(entityManager, this, columnInfoMap.get(name),
                            getFk(name), getRelationType(name)));
        }

        for (String name : addColumnNames) {
            addColumnMap.put(name, new ColumnDto(entityManager, this,
                    columnInfoMap.get(name)));
        }

        for (String name : editColumnNames) {
            boolean readOnly = readOnlyColumnNames.contains(name);
            editColumnMap.put(name, new ColumnDto(entityManager, this,
                    columnInfoMap.get(name), readOnly));

            if (!readOnly) {
                updatableColumnNames.add(name);
            }
        }

        for (String name : hiddenColumnNames) {
            updatableColumnNames.add(name);
        }
    }

    public YsMaintenanceFk getFk(String columnName) {
        return fkMap.get(columnName);
    }

    protected RelationType getRelationType(String columnName) {
        return relationTypeMap.get(columnName);
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

    public ConditionBean buildConditionBean(Map<String, String[]> map) {
        ConditionBean cb = newConditionBean();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            String name = entry.getKey();
            String[] value = entry.getValue();
            if (value.length == 0) {
                continue;
            }

            ColumnInfo columnInfo = getColumnInfo(name);
            if (columnInfo == null) {
                continue;
            }

            if (Date.class.isAssignableFrom(columnInfo.getPropertyType())) {
                if (value.length >= 2) {
                    entityManager.setValue(cb, name,
                            ConditionKey.CK_GREATER_EQUAL, value[0]);
                    entityManager.setValue(cb, name,
                            ConditionKey.CK_GREATER_EQUAL, value[1]);
                } else {
                    entityManager.setValue(cb, name,
                            ConditionKey.CK_LESS_EQUAL, value[0]);
                }
            } else {
                entityManager.setValue(cb, name, ConditionKey.CK_EQUAL, value);
            }
        }
        return cb;
    }

    public Map<String, ColumnDto> getColumnMap(Pane pane) {
        switch (pane) {
        case SEARCH:
            return searchColumnMap;

        case LIST:
            return listColumnMap;

        case DETAIL:
            return detailColumnMap;

        case ADD:
            return addColumnMap;

        case EDIT:
            return editColumnMap;

        default:
            throw new IllegalArgumentException("Illegal pane: " + pane);
        }
    }

    public Set<String> getHiddenColumnNames(Pane pane) {
        if (pane == Pane.EDIT) {
            return hiddenColumnNames;
        } else {
            return Collections.emptySet();
        }
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

    public String getColumnValueLabel(Entity entity, String columnName) {
        String property = columnName;
        YsMaintenanceFk fk = getFk(columnName);
        if (fk != null) {
            YsMaintenanceEntity entityAnn = fk.foreignEntity().getAnnotation(
                    YsMaintenanceEntity.class);
            if (entityAnn != null) {
                property = entityManager.getDBMeta(fk.foreignEntity())
                        .getTablePropertyName()
                        + "." + entityAnn.labelColumn();
            } else {
                property = entityManager.getDBMeta(fk.foreignEntity())
                        .getTablePropertyName()
                        + "." + fk.foreignColumn();
            }
        }

        PropertyHandler handler = typeConversionManager.getPropertyHandler(
                entity, property);
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

    public boolean isIncludedColumn(Pane pane, String columnName) {
        return getColumnMap(pane).containsKey(columnName);
    }

    public boolean isReadOnlyColumn(String columnName) {
        return readOnlyColumnNames.contains(columnName);
    }

    public Collection<String> getSearchColumnNames() {
        return searchColumnMap.keySet();
    }

    public String getLabelColumnName() {
        return labelColumnName;
    }

    public Set<String> getForeignColumnNames() {
        return fkMap.keySet();
    }

    public Set<String> getForeignColumnNames(RelationType relationType) {
        switch (relationType) {
        case HAS_ONE:
            return hasOneFkMap.keySet();

        case HAS_MANY:
            return hasManyFkMap.keySet();

        case BELONGS_TO:
            return belongsToFkMap.keySet();

        default:
            throw new IllegalArgumentException("Unknown relation type: "
                    + relationType);
        }
    }

    public List<ForeignInfo> getForeignInfos(String columnName) {
        return getColumnInfo(columnName).getForeignInfoList();
    }

    public Set<String> getUpdatableColumnNames(Pane pane) {
        switch (pane) {
        case ADD:
            return addColumnMap.keySet();

        case EDIT:
            return updatableColumnNames;

        default:
            return Collections.emptySet();
        }
    }
}
