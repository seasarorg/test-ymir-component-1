package org.seasar.ymir.extension.creator.action.impl;

import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassType;
import org.seasar.ymir.extension.creator.PropertyDesc;

public class ClassDescDto {

    private String name_;

    private PropertyDescDto[] propertyDescs_;

    private boolean checked_;

    private boolean dto_;

    private String pairClass_;

    public ClassDescDto(ClassDesc classDesc, boolean checked) {
        name_ = classDesc.getName();
        PropertyDesc[] pds = classDesc.getPropertyDescs();
        propertyDescs_ = new PropertyDescDto[pds.length];
        for (int i = 0; i < pds.length; i++) {
            propertyDescs_[i] = new PropertyDescDto(pds[i]);
        }
        checked_ = checked;
        dto_ = classDesc.isTypeOf(ClassType.DTO);
        pairClass_ = toString(classDesc.getMetaClassValues("conversion"));
    }

    String toString(Class<?>[] classes) {
        if (classes == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (int i = 0; i < classes.length; i++) {
            sb.append(delim).append(classes[i].getName().replace('$', '.'));
            delim = ",";
        }
        return sb.toString();
    }

    public boolean isChecked() {
        return checked_;
    }

    public String getName() {
        return name_;
    }

    public PropertyDescDto[] getPropertyDescs() {
        return propertyDescs_;
    }

    public boolean isDto() {
        return dto_;
    }

    public String getPairClass() {
        return pairClass_;
    }
}
