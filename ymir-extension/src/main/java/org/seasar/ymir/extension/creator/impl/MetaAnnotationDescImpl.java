package org.seasar.ymir.extension.creator.impl;

import org.seasar.kvasir.util.StringUtils;
import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.extension.creator.MetaAnnotationDesc;

public class MetaAnnotationDescImpl implements MetaAnnotationDesc {
    private String metaName_;

    private String[] metaValue_;

    private Class<?>[] metaClassValue_;

    public MetaAnnotationDescImpl(Meta meta) {
        this(meta.name(), meta.value(), meta.classValue());
    }

    public MetaAnnotationDescImpl(String metaName, String[] metaValue,
            Class<?>[] metaClassValue) {
        metaName_ = metaName;
        metaValue_ = metaValue;
        metaClassValue_ = metaClassValue;
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
        StringBuilder sb = new StringBuilder();
        sb.append("(name=\"").append(metaName_).append("\"");
        if (metaValue_.length > 0) {
            sb.append(",value=").append(toLiteral(metaValue_));
        }
        if (metaClassValue_.length > 0) {
            sb.append(",classValue=").append(toLiteral(metaClassValue_));
        }
        sb.append(")");
        return sb.toString();
    }

    protected String toLiteral(Object[] objs) {
        if (objs == null) {
            return String.valueOf(null);
        } else if (objs.length == 1) {
            return toLiteral(objs[0]);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            String delim = "";
            for (int i = 0; i < objs.length; i++) {
                sb.append(delim).append(toLiteral(objs[i]));
                delim = ", ";
            }
            sb.append("}");
            return sb.toString();
        }
    }

    protected String toLiteral(Object obj) {
        if (obj == null) {
            return String.valueOf(null);
        } else if (obj instanceof Class) {
            return ((Class<?>) obj).getName() + ".class";
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
}
