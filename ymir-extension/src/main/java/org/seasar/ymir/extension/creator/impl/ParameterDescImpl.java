package org.seasar.ymir.extension.creator.impl;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.TreeSet;

import org.seasar.ymir.extension.creator.Desc;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.ParameterDesc;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.util.DescUtils;

public class ParameterDescImpl implements ParameterDesc {
    private DescPool pool_;

    private TypeDesc typeDesc_;

    private String name_;

    private Desc<?> parent_;

    public ParameterDescImpl(DescPool pool) {
        pool_ = pool;
    }

    public ParameterDescImpl(DescPool pool, Type type) {
        this(pool, type, null);
    }

    public ParameterDescImpl(DescPool pool, TypeDesc typeDesc) {
        this(pool, typeDesc, null);
    }

    public ParameterDescImpl(DescPool pool, Type type, String name) {
        this(pool, pool.newTypeDesc(type), name);
    }

    public ParameterDescImpl(DescPool pool, TypeDesc typeDesc, String name) {
        pool_ = pool;
        setTypeDesc(typeDesc);
        name_ = name;
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
        typeDesc_.setParent(this);
    }

    public void setTypeDesc(Type type) {
        setTypeDesc(pool_.newTypeDesc(type));
    }

    public void setTypeDesc(String typeName) {
        setTypeDesc(pool_.newTypeDesc(typeName));
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

    public ParameterDesc transcriptTo(ParameterDesc desc) {
        desc.setTypeDesc(typeDesc_.transcriptTo(desc.getDescPool().newTypeDesc(
                typeDesc_)));
        desc.setName(name_);

        return desc;
    }

    @SuppressWarnings("unchecked")
    public <D extends Desc<?>> D getParent() {
        return (D) parent_;
    }

    public void setParent(Desc<?> parent) {
        parent_ = parent;
    }

    public void addDependingClassNamesTo(Set<String> set) {
        typeDesc_.addDependingClassNamesTo(set);
    }

    public String[] getImportClassNames() {
        Set<String> set = new TreeSet<String>();
        addDependingClassNamesTo(set);

        DescUtils.removeStandardClassNames(set);
        return set.toArray(new String[0]);
    }

    public void setTouchedClassNameSet(Set<String> set) {
        typeDesc_.setTouchedClassNameSet(set);
    }
}
