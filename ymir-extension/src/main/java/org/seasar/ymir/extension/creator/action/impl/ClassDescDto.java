package org.seasar.ymir.extension.creator.action.impl;

import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.extension.Globals;
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

    private String[] undecidedParameterNames_;

    public ClassDescDto(ClassDesc classDesc, boolean checked) {
        name_ = classDesc.getName();
        checked_ = checked;
        dto_ = classDesc.isTypeOf(ClassType.DTO);
        page_ = classDesc.isTypeOf(ClassType.PAGE);
        pairTypeName_ = PropertyUtils
                .join(classDesc.getMetaValue("conversion"));
        String superclassName = classDesc.getSuperclassName();
        if (Object.class.getName().equals(superclassName)) {
            superclassName = null;
        }
        superclassName_ = superclassName;
        undecidedParameterNames_ = (String[]) classDesc
                .getAttribute(Globals.ATTR_UNDECIDEDPARAMETERNAMES);

        ClassDesc ownerPageClassDesc = (ClassDesc) classDesc
                .getAttribute(Globals.ATTR_OWNERPAGE);
        PropertyDesc[] pds = classDesc.getPropertyDescs();
        propertyDescs_ = new PropertyDescDto[pds.length];
        for (int i = 0; i < pds.length; i++) {
            propertyDescs_[i] = new PropertyDescDto(pds[i],
                    ownerPageClassDesc != null
                            && ownerPageClassDesc.getPropertyDesc(pds[i]
                                    .getName()) != null);
        }
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

    public String[] getUndecidedParameterNames() {
        return undecidedParameterNames_;
    }
}
