package org.seasar.ymir.extension.zpt;

import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;

public class DescWrapper {

    private AnalyzerContext analyzerContext_;

    private ClassDesc classDesc_;

    private ClassDesc propertyTypeClassDesc_;

    private PropertyDesc propertyDesc_;

    private boolean array_;

    public DescWrapper(ClassDesc propertyTypeClassDesc) {
        propertyTypeClassDesc_ = propertyTypeClassDesc;
    }

    public DescWrapper(AnalyzerContext analyzerContext, ClassDesc classDesc,
            PropertyDesc propertyDesc) {
        analyzerContext_ = analyzerContext;
        classDesc_ = classDesc;
        propertyDesc_ = propertyDesc;
        array_ = propertyDesc.getTypeDesc().isArray();
    }

    public String toString() {
        return "0";
    }

    public ClassDesc getClassDesc() {
        return classDesc_;
    }

    public PropertyDesc getPropertyDesc() {
        return propertyDesc_;
    }

    public ClassDesc getPropertyTypeClassDesc() {
        if (propertyTypeClassDesc_ != null) {
            return propertyTypeClassDesc_;
        } else {
            return analyzerContext_.preparePropertyTypeClassDesc(classDesc_,
                    propertyDesc_, true);
        }
    }

    public boolean isArray() {
        return array_;
    }
}
