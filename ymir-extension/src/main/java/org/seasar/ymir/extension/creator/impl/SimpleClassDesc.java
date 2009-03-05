package org.seasar.ymir.extension.creator.impl;

import org.seasar.ymir.extension.creator.AbstractClassDesc;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;

public class SimpleClassDesc extends AbstractClassDesc {
    private String name_;

    public SimpleClassDesc(String name) {
        name_ = name;
    }

    public Object clone() {
        return this;
    }

    public String getName() {
        return name_;
    }

    public void setName(String name) {
        throw new UnsupportedOperationException();
    }

    public MethodDesc[] getMethodDescs() {
        return new MethodDesc[0];
    }

    public MethodDesc[] getMethodDescsOrderByName() {
        return new MethodDesc[0];
    }

    public PropertyDesc[] getPropertyDescs() {
        return new PropertyDesc[0];
    }

    public PropertyDesc[] getPropertyDescsOrderByName() {
        return new PropertyDesc[0];
    }

    public MethodDesc getMethodDesc(MethodDesc methodDesc) {
        return null;
    }

    public void setPropertyDesc(PropertyDesc propertyDesc) {
        throw new UnsupportedOperationException();
    }

    public PropertyDesc addProperty(String name, int mode) {
        throw new UnsupportedOperationException();
    }

    public void merge(ClassDesc classDesc, boolean mergeMethod) {
        throw new UnsupportedOperationException();
    }

    public void setMethodDesc(MethodDesc methodDesc) {
        throw new UnsupportedOperationException();
    }

    public void setSuperclassName(String superclassName) {
        throw new UnsupportedOperationException();
    }

    public String getSuperclassName() {
        return Object.class.getName();
    }

    public boolean isBaseClassAbstract() {
        return false;
    }

    public void setBaseClassAbstract(boolean baseClassAbstract) {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        return true;
    }

    public PropertyDesc getPropertyDesc(String name) {
        return null;
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public void merge(ClassDesc classDesc) {
        throw new UnsupportedOperationException();
    }

    public void removeMethodDesc(MethodDesc methodDesc) {
    }

    public void removePropertyDesc(String name) {
    }

    public void setPropertyDescs(PropertyDesc[] propertyDescs) {
        throw new UnsupportedOperationException();
    }

    public void setMethodDescs(MethodDesc[] methodDescs) {
        throw new UnsupportedOperationException();
    }

    public void removeBornOfFromAllMembers(String bornOf) {
        throw new UnsupportedOperationException();
    }

    public void applyBornOfToAllMembers() {
        throw new UnsupportedOperationException();
    }

    public String getPathOfClass() {
        return null;
    }
}
