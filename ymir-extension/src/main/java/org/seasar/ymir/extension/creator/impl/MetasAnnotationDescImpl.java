package org.seasar.ymir.extension.creator.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.annotation.Metas;
import org.seasar.ymir.extension.creator.MetaAnnotationDesc;
import org.seasar.ymir.extension.creator.MetasAnnotationDesc;
import org.seasar.ymir.util.ClassUtils;

public class MetasAnnotationDescImpl implements MetasAnnotationDesc {
    private MetaAnnotationDesc[] metaAnnotationDescs_;

    private Set<String> touchedClassNameSet_ = new HashSet<String>();

    public MetasAnnotationDescImpl(Metas metas) {
        List<MetaAnnotationDesc> list = new ArrayList<MetaAnnotationDesc>(metas
                .value().length);
        for (Meta m : metas.value()) {
            list.add(new MetaAnnotationDescImpl(m));
        }
        metaAnnotationDescs_ = list.toArray(new MetaAnnotationDesc[0]);
    }

    public MetasAnnotationDescImpl(MetaAnnotationDesc[] mads) {
        metaAnnotationDescs_ = mads;
    }

    public Object clone() {
        MetasAnnotationDescImpl cloned;
        try {
            cloned = (MetasAnnotationDescImpl) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }

        cloned.metaAnnotationDescs_ = new MetaAnnotationDesc[metaAnnotationDescs_.length];
        System.arraycopy(metaAnnotationDescs_, 0, cloned.metaAnnotationDescs_,
                0, metaAnnotationDescs_.length);
        return cloned;
    }

    @Override
    public String toString() {
        return getAsString();
    }

    public String getAsString() {
        return "@" + getName() + getBody();
    }

    public String getAsShortString() {
        touchedClassNameSet_.add(getName());
        return "@" + ClassUtils.getShortName(getName()) + getShortBody();
    }

    public String getBody() {
        return getBody0(false);
    }

    public String getShortBody() {
        return getBody0(true);
    }

    protected String getBody0(boolean shorten) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        if (metaAnnotationDescs_.length > 1) {
            sb.append("{");
        }
        String delim = "";
        for (int i = 0; i < metaAnnotationDescs_.length; i++) {
            sb.append(delim).append(
                    shorten ? metaAnnotationDescs_[i].getAsShortString()
                            : metaAnnotationDescs_[i].getAsString());
            delim = ", ";
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
        for (int i = 0; i < metaAnnotationDescs_.length; i++) {
            String[] values = metaAnnotationDescs_[i].getValues(name);
            if (values != null) {
                return values;
            }
        }
        return null;
    }

    public Class<?>[] getClassValues(String name) {
        for (int i = 0; i < metaAnnotationDescs_.length; i++) {
            Class<?>[] classValues = metaAnnotationDescs_[i]
                    .getClassValues(name);
            if (classValues != null) {
                return classValues;
            }
        }
        return null;
    }

    public MetaAnnotationDesc[] getMetaAnnotationDescs() {
        return metaAnnotationDescs_;
    }

    public String getMetaName() {
        throw new UnsupportedOperationException();
    }

    public void addDependingClassNamesTo(Set<String> set) {
        set.add(Metas.class.getName());
        for (MetaAnnotationDesc metaAnnotationDesc : metaAnnotationDescs_) {
            metaAnnotationDesc.addDependingClassNamesTo(set);
        }
    }

    public void setTouchedClassNameSet(Set<String> set) {
        touchedClassNameSet_ = set;

        for (MetaAnnotationDesc metaAnnotationDesc : metaAnnotationDescs_) {
            metaAnnotationDesc.setTouchedClassNameSet(set);
        }
    }
}
