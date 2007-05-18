package org.seasar.ymir.extension.zpt;

import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;

public class DescWrapper {

    private AnalyzerContext analyzerContext_;

    private ClassDesc classDesc_;

    private PropertyDesc propertyDesc_;

    private boolean array_;

    public DescWrapper(ClassDesc classDesc) {

        classDesc_ = classDesc;
    }

    public DescWrapper(AnalyzerContext analyzerContext,
            PropertyDesc propertyDesc) {

        analyzerContext_ = analyzerContext;
        propertyDesc_ = propertyDesc;
        array_ = propertyDesc.getTypeDesc().isArray();
    }

    public String toString() {

        return "0";
    }

    public ClassDesc getClassDesc() {

        if (classDesc_ != null) {
            return classDesc_;
        } else {
            return analyzerContext_.preparePropertyTypeClassDesc(propertyDesc_,
                    true);
        }
    }

    public PropertyDesc getPropertyDesc() {

        return propertyDesc_;
    }

    public boolean isArray() {

        return array_;
    }
}
