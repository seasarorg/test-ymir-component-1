package org.seasar.ymir.extension.creator.impl;

import java.lang.reflect.Type;

import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.ParameterDesc;
import org.seasar.ymir.extension.creator.TypeDesc;

public class ParameterDescImpl implements ParameterDesc {
    private DescPool pool_;

    private TypeDesc typeDesc_;

    private String name_;

    public ParameterDescImpl(DescPool pool, Type type) {
        this(pool, type, null);
    }

    public ParameterDescImpl(DescPool pool, TypeDesc typeDesc) {
        this(pool, typeDesc, null);
    }

    public ParameterDescImpl(DescPool pool, Type type, String name) {
        this(pool, pool != null ? pool.newTypeDesc(type) : new TypeDescImpl(
                null, type), name);
    }

    public ParameterDescImpl(DescPool pool, TypeDesc typeDesc, String name) {
        pool_ = pool;
        setTypeDesc(typeDesc);
        name_ = name;
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

    public DescPool getDescPool() {
        return pool_;
    }

    public TypeDesc getTypeDesc() {
        return typeDesc_;
    }

    public void setTypeDesc(TypeDesc typeDesc) {
        typeDesc_ = typeDesc;
    }

    public void setTypeDesc(Type type) {
        if (pool_ != null) {
            setTypeDesc(pool_.newTypeDesc(type));
        } else {
            setTypeDesc(new TypeDescImpl(null, type));
        }
    }

    public void setTypeDesc(String typeName) {
        if (pool_ != null) {
            setTypeDesc(pool_.newTypeDesc(typeName));
        } else {
            setTypeDesc(new TypeDescImpl(null, typeName));
        }
    }

    public String getName() {
        if (name_ != null) {
            return name_;
        } else {
            return getTypeDesc().getInstanceName();
        }
    }

    public String getNameAsIs() {
        return name_;
    }

    public void setName(String name) {
        name_ = name;
    }
}
