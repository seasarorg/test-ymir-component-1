package org.seasar.ymir.extension.creator.impl;

import org.seasar.ymir.extension.creator.ParameterDesc;
import org.seasar.ymir.extension.creator.TypeDesc;

public class ParameterDescImpl implements ParameterDesc {

    private TypeDesc typeDesc_;

    private String name_;

    public ParameterDescImpl(TypeDesc typeDesc) {
        this(typeDesc, null);
    }

    public ParameterDescImpl(TypeDesc typeDesc, String name) {
        setTypeDesc(typeDesc);
        name_ = name;
    }

    public ParameterDescImpl(Class type) {
        this(type, null);
    }

    public ParameterDescImpl(Class type, String name) {
        this(new TypeDescImpl(type), name);
    }

    public Object clone() {

        ParameterDescImpl cloned;
        try {
            cloned = (ParameterDescImpl) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
        if (cloned.typeDesc_ != null) {
            cloned.typeDesc_ = (TypeDesc) typeDesc_.clone();
        }
        return cloned;
    }

    public String toString() {

        return typeDesc_ + " " + name_;
    }

    public TypeDesc getTypeDesc() {

        return typeDesc_;
    }

    public void setTypeDesc(TypeDesc typeDesc) {

        typeDesc_ = typeDesc;
    }

    public String getName() {

        if (name_ != null) {
            return name_;
        } else {
            return getTypeDesc().getInstanceName();
        }
    }

    public void setName(String name) {

        name_ = name;
    }
}
