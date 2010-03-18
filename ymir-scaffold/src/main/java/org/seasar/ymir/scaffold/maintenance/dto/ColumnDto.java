package org.seasar.ymir.scaffold.maintenance.dto;

import java.util.List;

import org.seasar.dbflute.dbmeta.info.ColumnInfo;
import org.seasar.dbflute.dbmeta.info.ForeignInfo;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.message.Messages;
import org.seasar.ymir.scaffold.maintenance.annotation.YsMaintenanceFK;

public class ColumnDto {
    private Messages messages;

    private ColumnInfo columnInfo;

    private String name;

    private YsMaintenanceFK fk;

    private boolean readOnly;

    public ColumnDto(ColumnInfo columnInfo) {
        this(columnInfo, false);
    }

    public ColumnDto(ColumnInfo columnInfo, boolean readOnly) {
        this(columnInfo, null, readOnly);
    }

    public ColumnDto(ColumnInfo columnInfo, YsMaintenanceFK fk) {
        this(columnInfo, fk, false);
    }

    public ColumnDto(ColumnInfo columnInfo, YsMaintenanceFK fk, boolean readOnly) {
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

    public String getLabel() {
        if (fk != null) {
            return fk.foreignDisplayColumnLabel();
        } else {
            return getMessages().getMessage("label." + name);
        }
    }

    protected Messages getMessages() {
        if (messages == null) {
            messages = (Messages) YmirContext.getYmir().getApplication()
                    .getS2Container().getComponent(Messages.class);
        }
        return messages;
    }

    public boolean isReadOnly() {
        return readOnly;
    }
}
