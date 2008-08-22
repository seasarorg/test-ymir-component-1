package org.seasar.ymir.extension.creator.action.impl;

import org.seasar.ymir.util.BeanUtils;

public class PropertyDto implements Comparable<PropertyDto> {
    private String name_;

    public PropertyDto(String name) {
        name_ = name;
    }

    @Override
    public int hashCode() {
        return name_ != null ? name_.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        PropertyDto o = (PropertyDto) obj;
        if (o.name_ == null) {
            if (name_ != null) {
                return false;
            }
        } else {
            if (!o.name_.equals(name_)) {
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return name_;
    }

    public String getNormalizedName() {
        return BeanUtils.normalizePropertyName(name_);
    }

    public int compareTo(PropertyDto o) {
        if (name_ == null) {
            if (o.name_ == null) {
                return 0;
            } else {
                return -1;
            }
        } else {
            if (o.name_ == null) {
                return 1;
            } else {
                return name_.compareTo(o.name_);
            }
        }
    }
}
