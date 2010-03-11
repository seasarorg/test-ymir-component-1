package org.seasar.ymir.dbflute;

import java.util.Locale;

import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.dbmeta.info.ColumnInfo;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.message.MessageProvider;

public class EntityMessageProvider implements MessageProvider {
    private static final String PREFIX_LABEL = "label.";

    @Binding(bindingType = BindingType.MUST)
    protected EntityManager entityManager;

    public String getMessageValue(String name, Locale locale) {
        return getMessageValue(name);
    }

    public String getMessageValue(String name) {
        if (!name.startsWith(PREFIX_LABEL)) {
            return null;
        }

        String entityName = name.substring(PREFIX_LABEL.length());
        int dot = entityName.indexOf('.');
        if (dot >= 0) {
            String columnName = entityName.substring(dot + 1);
            entityName = entityName.substring(0, dot);
            ColumnInfo columnInfo = entityManager.getColumnInfo(entityName,
                    columnName);
            if (columnInfo == null) {
                return null;
            }
            String label = columnInfo.getColumnAlias();
            if (label == null) {
                label = columnName;
            }
            return label;
        } else {
            DBMeta meta = entityManager.getDBMeta(entityName);
            if (meta == null) {
                return null;
            }
            String label = meta.getTableAlias();
            if (label == null) {
                label = entityName;
            }
            return label;
        }
    }
}
