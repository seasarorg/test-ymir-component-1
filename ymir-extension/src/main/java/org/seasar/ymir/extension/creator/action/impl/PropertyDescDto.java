package org.seasar.ymir.extension.creator.action.impl;

import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.util.BeanUtils;

public class PropertyDescDto {

    private String name_;

    private String typeName_;

    public PropertyDescDto(PropertyDesc pd) {
        name_ = pd.getName();
        typeName_ = pd.getTypeDesc().getName();
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
}
