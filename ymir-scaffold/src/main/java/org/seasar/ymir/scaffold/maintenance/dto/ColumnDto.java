package org.seasar.ymir.scaffold.maintenance.dto;

import java.util.List;

import org.seasar.dbflute.dbmeta.info.ColumnInfo;
import org.seasar.dbflute.dbmeta.info.ForeignInfo;
import org.seasar.ymir.scaffold.maintenance.annotation.MaintenanceFK;

public class ColumnDto {
    private ColumnInfo columnInfo;

    private String name;

    private MaintenanceFK fk;

    private boolean readOnly;

    public ColumnDto(ColumnInfo columnInfo) {
        this(columnInfo, false);
    }

    public ColumnDto(ColumnInfo columnInfo, boolean readOnly) {
        this(columnInfo, null, readOnly);
    }

    public ColumnDto(ColumnInfo columnInfo, MaintenanceFK fk) {
        this(columnInfo, fk, false);
    }

    public ColumnDto(ColumnInfo columnInfo, MaintenanceFK fk, boolean readOnly) {
        this.columnInfo = columnInfo;
        name = columnInfo.getPropertyName();
        this.fk = fk;
        this.readOnly = readOnly;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        if (fk != null) {
            List<ForeignInfo> foreignInfos = columnInfo.getForeignInfoList();
            if (!foreignInfos.isEmpty()) {
                return "entity/" + foreignInfos.get(0).getForeignPropertyName()
                        + "/" + fk.foreignDisplayColumn();
            }
        }

        return "entity/" + name;
    }

    public String getLabelPath() {
        if (fk != null) {
            return "string:" + fk.foreignDisplayColumnLabel();
        } else {
            return "messages/%label." + name;
        }
    }

    public boolean isReadOnly() {
        return readOnly;
    }
}
