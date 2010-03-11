package org.seasar.ymir.scaffold.maintenance.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.dbmeta.info.ColumnInfo;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.scaffold.maintenance.Constants;
import org.seasar.ymir.scaffold.maintenance.annotation.MaintenanceAdd;
import org.seasar.ymir.scaffold.maintenance.annotation.MaintenanceEdit;
import org.seasar.ymir.scaffold.maintenance.annotation.MaintenanceEntity;
import org.seasar.ymir.scaffold.maintenance.annotation.MaintenanceIndex;
import org.seasar.ymir.scaffold.maintenance.enm.Action;

public class FormDto implements Constants {
    private List<ColumnDto> columns;

    private List<String> hiddenColumnNames;

    private List<String> updatableColumnNames;

    public FormDto(AnnotationHandler annotationHandler, Class<?> pageClass,
            DBMeta meta, Action action) {
        String createdDateColumn = DEFAULT_COLUMN_CREATED_DATE;
        String modifiedDateColumn = DEFAULT_COLUMN_MODIFIED_DATE;
        MaintenanceEntity entityAnn = annotationHandler.getAnnotation(
                pageClass, MaintenanceEntity.class);
        if (entityAnn != null) {
            createdDateColumn = entityAnn.createdDateColumn();
            modifiedDateColumn = entityAnn.modifiedDateColumn();
        }

        Set<String> columnNames = new LinkedHashSet<String>();
        Set<String> excludeColumnNames = new HashSet<String>();
        Set<String> readOnlyColumnNames = new HashSet<String>();
        if (action == Action.INDEX) {
            MaintenanceIndex ann = annotationHandler.getAnnotation(pageClass,
                    MaintenanceIndex.class);
            if (ann != null) {
                columnNames.addAll(Arrays.asList(ann.columnsOrder()));
                excludeColumnNames.addAll(Arrays.asList(ann.excludeColumns()));
            }
        } else if (action == Action.ADD) {
            MaintenanceAdd ann = annotationHandler.getAnnotation(pageClass,
                    MaintenanceAdd.class);
            if (ann != null) {
                columnNames.addAll(Arrays.asList(ann.columnsOrder()));
            }
        } else if (action == Action.EDIT) {
            MaintenanceEdit ann = annotationHandler.getAnnotation(pageClass,
                    MaintenanceEdit.class);
            if (ann != null) {
                columnNames.addAll(Arrays.asList(ann.columnsOrder()));
                excludeColumnNames.addAll(Arrays.asList(ann.excludeColumns()));
                readOnlyColumnNames
                        .addAll(Arrays.asList(ann.readOnlyColumns()));
            }
        }

        Map<String, ColumnInfo> columnInfoMap = new HashMap<String, ColumnInfo>();
        hiddenColumnNames = new ArrayList<String>();
        updatableColumnNames = new ArrayList<String>();
        for (ColumnInfo columnInfo : meta.getColumnInfoList()) {
            String name = columnInfo.getPropertyName();

            columnInfoMap.put(name, columnInfo);

            if (!COLUMN_VERSION_NO.equals(name)) {
                columnNames.add(name);
            }

            if (columnInfo.isPrimary()) {
                readOnlyColumnNames.add(name);
                hiddenColumnNames.add(name);
                updatableColumnNames.add(name);
            }
        }

        if (action == Action.ADD || action == Action.EDIT) {
            columnNames.remove(createdDateColumn);
            columnNames.remove(modifiedDateColumn);
        }

        columns = new ArrayList<ColumnDto>();
        for (String name : columnNames) {
            if (!excludeColumnNames.contains(name)) {
                boolean readOnly = readOnlyColumnNames.contains(name);
                columns.add(new ColumnDto(columnInfoMap.get(name), readOnly));
                if (!readOnly) {
                    updatableColumnNames.add(name);
                }
            }
        }

    }

    public List<ColumnDto> getColumns() {
        return columns;
    }

    public List<String> getHiddenColumnNames() {
        return hiddenColumnNames;
    }

    public List<String> getUpdatableColumnNames() {
        return updatableColumnNames;
    }
}
