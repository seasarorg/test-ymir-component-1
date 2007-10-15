package org.seasar.ymir.extension.creator.impl;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.extension.creator.MetaAnnotationDesc;

public class MetaAnnotationDescImpl implements MetaAnnotationDesc {
    private String metaName_;

    private String metaValue_;

    public MetaAnnotationDescImpl(Meta meta) {
        this(meta.name(), meta.value());
    }

    public MetaAnnotationDescImpl(String metaName, String metaValue) {
        metaName_ = metaName;
        metaValue_ = metaValue;
    }

    public Object clone() {
        try {
            return (MetaAnnotationDescImpl) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String toString() {
        return getString();
    }

    public String getString() {
        return "@" + getName() + getBody();
    }

    public String getBody() {
        return "(name=\"" + metaName_ + "\",value=\"" + metaValue_ + "\")";
    }

    public String getName() {
        return Meta.class.getName();
    }

    public void setBody(String body) {
        throw new UnsupportedOperationException();
    }

    public String getValue(String name) {
        if (name.equals(metaName_)) {
            return metaValue_;
        } else {
            return null;
        }
    }

    public boolean hasValue(String name) {
        return name.equals(metaName_);
    }

    public String[] getValues(String name) {
        String value = getValue(name);
        if (value != null) {
            return new String[] { value };
        } else {
            return new String[0];
        }
    }
}
