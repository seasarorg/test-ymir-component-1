package org.seasar.ymir.scaffold.maintenance.dto;

import org.seasar.dbflute.dbmeta.info.ColumnInfo;

public class ColumnDto {
    private String name;

    private boolean readOnly;

    public ColumnDto(ColumnInfo columnInfo) {
        this(columnInfo, false);
    }

    public ColumnDto(ColumnInfo columnInfo, boolean readOnly) {
        name = columnInfo.getPropertyName();
        this.readOnly = readOnly;
    }

    public String getName() {
        return name;
    }

    public boolean isReadOnly() {
        return readOnly;
    }
}
