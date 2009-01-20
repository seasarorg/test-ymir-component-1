package org.seasar.ymir.extension.creator.impl;

import java.util.Map;
import java.util.TreeMap;

import org.seasar.ymir.extension.creator.AbstractAnnotatedDesc;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.util.DescUtils;

public class PropertyDescImpl extends AbstractAnnotatedDesc implements
        PropertyDesc {
    public static final int NONE = 0;

    public static final int READ = 1;

    public static final int WRITE = 2;

    private String name_;

    private TypeDesc typeDesc_ = new TypeDescImpl();

    private int mode_;

    private boolean typeAlreadySet_;

    private Map<String, AnnotationDesc> annotationDescForGetterMap_ = new TreeMap<String, AnnotationDesc>();

    private Map<String, AnnotationDesc> annotationDescForSetterMap_ = new TreeMap<String, AnnotationDesc>();

    private String getterName_;

    public PropertyDescImpl(String name) {
        name_ = name;
    }

    public Object clone() {
        PropertyDescImpl cloned = (PropertyDescImpl) super.clone();

        if (typeDesc_ != null) {
            cloned.typeDesc_ = (TypeDesc) typeDesc_.clone();
        }
        cloned.annotationDescForGetterMap_ = new TreeMap<String, AnnotationDesc>(
                annotationDescForGetterMap_);
        cloned.annotationDescForSetterMap_ = new TreeMap<String, AnnotationDesc>(
                annotationDescForSetterMap_);

        return cloned;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(typeDesc_).append(" ").append(name_).append("(");
        if ((mode_ & READ) != 0) {
            sb.append("r");
        }
        if ((mode_ & WRITE) != 0) {
            sb.append("w");
        }
        sb.append(")");
        return sb.toString();
    }

    public String getName() {

        return name_;
    }

    public TypeDesc getTypeDesc() {

        return typeDesc_;
    }

    public void setTypeDesc(TypeDesc typeDesc) {

        typeDesc_ = typeDesc;
    }

    public void setTypeDesc(String typeName) {

        setTypeDesc(typeName, false);
    }

    public void setTypeDesc(String typeName, boolean explicit) {

        setTypeDesc(new TypeDescImpl(typeName, explicit));
    }

    public int getMode() {

        return mode_;
    }

    public void setMode(int mode) {

        mode_ = mode;
    }

    public void addMode(int mode) {

        mode_ |= mode;
    }

    public boolean isReadable() {

        return ((mode_ & READ) != 0);
    }

    public boolean isWritable() {

        return ((mode_ & WRITE) != 0);
    }

    public String getGetterName() {
        return getterName_ != null ? getterName_ : constructGetterName();
    }

    public void setGetterName(String getterName) {
        getterName_ = getterName;
    }

    String constructGetterName() {
        if ("boolean".equals(typeDesc_.getName())) {
            return "is" + capFirst(name_);
        } else {
            return "get" + capFirst(name_);
        }
    }

    String capFirst(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public boolean isTypeAlreadySet() {
        return typeAlreadySet_;
    }

    public void notifyUpdatingType() {
        typeAlreadySet_ = true;
    }

    public AnnotationDesc getAnnotationDescForGetter(String name) {
        return annotationDescForGetterMap_.get(name);
    }

    public void setAnnotationDescForGetter(AnnotationDesc annotationDesc) {
        DescUtils
                .setAnnotationDesc(annotationDescForGetterMap_, annotationDesc);
    }

    public AnnotationDesc[] getAnnotationDescsForGetter() {
        return annotationDescForGetterMap_.values().toArray(
                new AnnotationDesc[0]);
    }

    public void setAnnotationDescsForGetter(AnnotationDesc[] annotationDescs) {
        annotationDescForGetterMap_.clear();
        for (AnnotationDesc annotationDesc : annotationDescs) {
            setAnnotationDescForGetter(annotationDesc);
        }
    }

    public AnnotationDesc getAnnotationDescForSetter(String name) {
        return annotationDescForSetterMap_.get(name);
    }

    public void setAnnotationDescForSetter(AnnotationDesc annotationDesc) {
        DescUtils
                .setAnnotationDesc(annotationDescForSetterMap_, annotationDesc);
    }

    public AnnotationDesc[] getAnnotationDescsForSetter() {
        return annotationDescForSetterMap_.values().toArray(
                new AnnotationDesc[0]);
    }

    public void setAnnotationDescsForSetter(AnnotationDesc[] annotationDescs) {
        annotationDescForSetterMap_.clear();
        for (AnnotationDesc annotationDesc : annotationDescs) {
            setAnnotationDescForSetter(annotationDesc);
        }
    }

    public void setAnnotationDescs(AnnotationDesc[] annotationDescs) {
        super.clear();
        for (AnnotationDesc annotationDesc : annotationDescs) {
            setAnnotationDesc(annotationDesc);
        }
    }

    public String getMetaFirstValueOnGetter(String name) {
        return DescUtils.getMetaFirstValue(annotationDescForGetterMap_, name);
    }

    public String getMetaFirstValueOnSetter(String name) {
        return DescUtils.getMetaFirstValue(annotationDescForSetterMap_, name);
    }

    public boolean hasMetaOnGetter(String name) {
        return DescUtils.hasMeta(annotationDescForGetterMap_, name);
    }

    public boolean hasMetaOnSetter(String name) {
        return DescUtils.hasMeta(annotationDescForSetterMap_, name);
    }
}
