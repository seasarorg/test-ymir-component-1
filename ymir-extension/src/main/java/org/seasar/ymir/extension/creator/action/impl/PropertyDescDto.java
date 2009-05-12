package org.seasar.ymir.extension.creator.action.impl;

import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.util.BeanUtils;

public class PropertyDescDto {

    private String name_;

    private String typeName_;

    private boolean modifiable_;

    private boolean subordinate_;

    private boolean fixed_;

    public PropertyDescDto(PropertyDesc pd, boolean subordinate) {
        name_ = pd.getName();
        typeName_ = pd.getTypeDesc().getName();

        if (pd.isTypeAlreadySet(PropertyDesc.PROBABILITY_MAXIMUM)) {
            modifiable_ = false;
            subordinate_ = false;
            fixed_ = true;
        } else {
            if (subordinate) {
                modifiable_ = false;
                subordinate_ = true;
                fixed_ = false;
            } else {
                modifiable_ = true;
                subordinate_ = false;
                fixed_ = false;
            }
        }
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

    public boolean isModifiable() {
        return modifiable_;
    }

    public boolean isSubordinate() {
        return subordinate_;
    }

    public boolean isFixed() {
        return fixed_;
    }
}
