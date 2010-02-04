package org.seasar.ymir.extension.creator.impl;

import java.util.HashSet;
import java.util.Set;

import org.seasar.kvasir.util.StringUtils;
import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.extension.creator.MetaAnnotationDesc;
import org.seasar.ymir.util.ClassUtils;

public class MetaAnnotationDescImpl implements MetaAnnotationDesc {
    private String metaName_;

    private String[] metaValue_;

    private Class<?>[] metaClassValue_;

    private Set<String> touchedClassNameSet_ = new HashSet<String>();

    public MetaAnnotationDescImpl(Meta meta) {
        this(meta.name(), meta.value(), meta.classValue());
    }

    public MetaAnnotationDescImpl(String metaName, String[] metaValue) {
        this(metaName, metaValue, new Class<?>[0]);
    }

    public MetaAnnotationDescImpl(String metaName, Class<?>[] metaClassValue) {
        this(metaName, new String[0], metaClassValue);
    }

    public MetaAnnotationDescImpl(String metaName, String[] metaValue,
            Class<?>[] metaClassValue) {
        metaName_ = metaName;
        metaValue_ = metaValue;
        metaClassValue_ = metaClassValue;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
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

    public String getMetaName() {
        return metaName_;
    }

    public String getBody() {
        return getBody0(false);
    }

    public String getShortBody() {
        return getBody0(true);
    }

    protected String getBody0(boolean shorten) {
        StringBuilder sb = new StringBuilder();
        sb.append("(name = \"").append(metaName_).append("\"");
        if (metaValue_.length > 0) {
            sb.append(", value = ").append(toLiteral(metaValue_, shorten));
        }
        if (metaClassValue_.length > 0) {
            sb.append(", classValue = ").append(
                    toLiteral(metaClassValue_, shorten));
        }
        sb.append(")");
        return sb.toString();
    }

    protected String toLiteral(Object[] objs, boolean shorten) {
        if (objs == null) {
            return String.valueOf(null);
        } else if (objs.length == 1) {
            return toLiteral(objs[0], shorten);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            String delim = "";
            for (int i = 0; i < objs.length; i++) {
                sb.append(delim).append(toLiteral(objs[i], shorten));
                delim = ", ";
            }
            sb.append("}");
            return sb.toString();
        }
    }

    protected String toLiteral(Object obj, boolean shorten) {
        if (obj == null) {
            return String.valueOf(null);
        } else if (obj instanceof Class<?>) {
            if (shorten) {
                touchedClassNameSet_.add(((Class<?>) obj).getName());
                return ClassUtils.getShortName((Class<?>) obj) + ".class";
            } else {
                return ((Class<?>) obj).getName() + ".class";
            }
        } else {
            return StringUtils.quoteString(obj.toString(), '"');
        }
    }

    public String getName() {
        return Meta.class.getName();
    }

    public void setBody(String body) {
        throw new UnsupportedOperationException();
    }

    public String getValue(String name) {
        if (name.equals(metaName_) && metaValue_.length > 0) {
            return metaValue_[0];
        } else {
            return null;
        }
    }

    public boolean hasValue(String name) {
        return name.equals(metaName_);
    }

    public String[] getValues(String name) {
        if (name.equals(metaName_)) {
            return metaValue_;
        } else {
            return null;
        }
    }

    public Class<?>[] getClassValues(String name) {
        if (name.equals(metaName_)) {
            return metaClassValue_;
        } else {
            return null;
        }
    }

    public void addDependingClassNamesTo(Set<String> set) {
        set.add(Meta.class.getName());
        for (Class<?> value : metaClassValue_) {
            set.add(value.getName());
        }
    }

    public void setTouchedClassNameSet(Set<String> set) {
        if (set == touchedClassNameSet_) {
            return;
        }

        touchedClassNameSet_ = set;
    }
}
