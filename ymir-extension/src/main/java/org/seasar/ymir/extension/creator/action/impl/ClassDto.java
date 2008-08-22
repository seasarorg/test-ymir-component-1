package org.seasar.ymir.extension.creator.action.impl;

import java.util.Set;
import java.util.TreeSet;

public class ClassDto implements Comparable<ClassDto> {
    private String name_;

    private Set<PropertyDto> propertySet_ = new TreeSet<PropertyDto>();

    public ClassDto(String name) {
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
        ClassDto o = (ClassDto) obj;
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

    public PropertyDto[] getProperties() {
        return propertySet_.toArray(new PropertyDto[0]);
    }

    public void addProperty(PropertyDto propertyDto) {
        propertySet_.add(propertyDto);
    }

    public int compareTo(ClassDto o) {
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
