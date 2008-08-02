package org.seasar.ymir.extension.creator.action.impl;

import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassType;
import org.seasar.ymir.extension.creator.PropertyDesc;

public class ClassDescDto {

    private String name_;

    private PropertyDescDto[] propertyDescs_;

    private boolean checked_;

    private boolean dto_;

    private String pairTypeName_;

    public ClassDescDto(ClassDesc classDesc, boolean checked) {
        name_ = classDesc.getName();
        PropertyDesc[] pds = classDesc.getPropertyDescs();
        propertyDescs_ = new PropertyDescDto[pds.length];
        for (int i = 0; i < pds.length; i++) {
            propertyDescs_[i] = new PropertyDescDto(pds[i]);
        }
        checked_ = checked;
        dto_ = classDesc.isTypeOf(ClassType.DTO);
        pairTypeName_ = PropertyUtils.join(classDesc.getMetaValues("conversion"));
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

    public String getPairTypeName() {
        return pairTypeName_;
    }
}
