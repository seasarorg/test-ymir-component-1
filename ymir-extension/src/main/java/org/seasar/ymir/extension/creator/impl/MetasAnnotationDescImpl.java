package org.seasar.ymir.extension.creator.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.annotation.Metas;
import org.seasar.ymir.extension.creator.MetaAnnotationDesc;

public class MetasAnnotationDescImpl implements MetaAnnotationDesc {
    private MetaAnnotationDescImpl[] metaAnnotationDescs_;

    public MetasAnnotationDescImpl(Metas metas) {
        Meta[] ms = metas.value();
        metaAnnotationDescs_ = new MetaAnnotationDescImpl[ms.length];
        for (int i = 0; i < ms.length; i++) {
            metaAnnotationDescs_[i] = new MetaAnnotationDescImpl(ms[i]);
        }
    }

    public Object clone() {
        try {
            MetasAnnotationDescImpl cloned = (MetasAnnotationDescImpl) super
                    .clone();
            cloned.metaAnnotationDescs_ = new MetaAnnotationDescImpl[metaAnnotationDescs_.length];
            System
                    .arraycopy(metaAnnotationDescs_, 0,
                            cloned.metaAnnotationDescs_, 0,
                            metaAnnotationDescs_.length);
            return cloned;
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
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        if (metaAnnotationDescs_.length > 1) {
            sb.append("{");
        }
        String delim = "";
        for (int i = 0; i < metaAnnotationDescs_.length; i++) {
            sb.append(delim).append(metaAnnotationDescs_[i].getString());
            delim = ",";
        }
        if (metaAnnotationDescs_.length > 1) {
            sb.append("}");
        }
        sb.append(")");
        return sb.toString();
    }

    public String getName() {
        return Metas.class.getName();
    }

    public void setBody(String body) {
        throw new UnsupportedOperationException();
    }

    public String getValue(String name) {
        for (int i = 0; i < metaAnnotationDescs_.length; i++) {
            String value = metaAnnotationDescs_[i].getValue(name);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    public boolean hasValue(String name) {
        for (int i = 0; i < metaAnnotationDescs_.length; i++) {
            if (metaAnnotationDescs_[i].hasValue(name)) {
                return true;
            }
        }
        return false;
    }

    public String[] getValues(String name) {
        List<String> valueList = new ArrayList<String>();
        for (int i = 0; i < metaAnnotationDescs_.length; i++) {
            valueList.addAll(Arrays.asList(metaAnnotationDescs_[i]
                    .getValues(name)));
        }
        return valueList.toArray(new String[0]);
    }
}
