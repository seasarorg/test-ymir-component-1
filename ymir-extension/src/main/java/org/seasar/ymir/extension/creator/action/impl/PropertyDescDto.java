package org.seasar.ymir.extension.creator.action.impl;

import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.util.BeanUtils;

public class PropertyDescDto {

    private String name_;

    private String typeName_;

    private boolean subordinate_;

    public PropertyDescDto(PropertyDesc pd, boolean subordinate) {
        name_ = pd.getName();
        typeName_ = pd.getTypeDesc().getName();
        subordinate_ = subordinate;
    }

    public String getName() {
        return name_;
    }

    public String getTypeName() {
        return typeName_;
    }

    public boolean isAmbiguous() {
        return BeanUtils.isAmbiguousPropertyName(name_);
    }

    public String getNormalizedName() {
        return BeanUtils.normalizePropertyName(name_);
    }

    public boolean isSubordinate() {
        return subordinate_;
    }
}
