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

    private boolean page_;

    private String pairTypeName_;

    private String superclassName_;

    public ClassDescDto(ClassDesc classDesc, boolean checked) {
        name_ = classDesc.getName();
        PropertyDesc[] pds = classDesc.getPropertyDescs();
        propertyDescs_ = new PropertyDescDto[pds.length];
        for (int i = 0; i < pds.length; i++) {
            propertyDescs_[i] = new PropertyDescDto(pds[i]);
        }
        checked_ = checked;
        dto_ = classDesc.isTypeOf(ClassType.DTO);
        page_ = classDesc.isTypeOf(ClassType.PAGE);
        pairTypeName_ = PropertyUtils.join(classDesc
                .getMetaValues("conversion"));
        String superclassName = classDesc.getSuperclassName();
        if (Object.class.getName().equals(superclassName)) {
            superclassName = null;
        }
        superclassName_ = superclassName;
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

    public boolean isPage() {
        return page_;
    }

    public String getPairTypeName() {
        return pairTypeName_;
    }

    public String getSuperclassName() {
        return superclassName_;
    }
}
