package org.seasar.ymir.scaffold.maintenance.dto;

import java.util.Date;

import org.seasar.dbflute.dbmeta.info.ColumnInfo;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.dbflute.EntityManager;
import org.seasar.ymir.message.Messages;
import org.seasar.ymir.scaffold.maintenance.annotation.YsMaintenanceFk;
import org.seasar.ymir.scaffold.maintenance.enm.RelationType;
import org.seasar.ymir.scaffold.maintenance.web.EntityBean;

public class ColumnDto {
    private Messages messages;

    private EntityManager entityManager;

    private EntityBean entityBean;

    private ColumnInfo columnInfo;

    private String name;

    private YsMaintenanceFk fk;

    private RelationType relationType;

    private boolean readOnly;

    public ColumnDto(EntityManager entityManager, EntityBean entityBean,
            ColumnInfo columnInfo) {
        this(entityManager, entityBean, columnInfo, false);
    }

    public ColumnDto(EntityManager entityManager, EntityBean entityBean,
            ColumnInfo columnInfo, boolean readOnly) {
        this(entityManager, entityBean, columnInfo, null, null, readOnly);
    }

    public ColumnDto(EntityManager entityManager, EntityBean entityBean,
            ColumnInfo columnInfo, YsMaintenanceFk fk, RelationType relationType) {
        this(entityManager, entityBean, columnInfo, fk, relationType, false);
    }

    public ColumnDto(EntityManager entityManager, EntityBean entityBean,
            ColumnInfo columnInfo, YsMaintenanceFk fk,
            RelationType relationType, boolean readOnly) {
        this.entityManager = entityManager;
        this.entityBean = entityBean;
        this.columnInfo = columnInfo;
        name = columnInfo.getPropertyName();
        this.fk = fk;
        this.relationType = relationType;
        this.readOnly = readOnly;
    }

    public String getName() {
        return name;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public String getLabel() {
        return getMessages().getMessage(
                "label." + entityBean.getEntityName() + "." + name);
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

    public boolean isDateType() {
        return Date.class.isAssignableFrom(columnInfo.getPropertyType());
    }
}
