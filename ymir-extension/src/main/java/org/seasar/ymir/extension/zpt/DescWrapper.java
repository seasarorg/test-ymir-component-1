package org.seasar.ymir.extension.zpt;

import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.TypeDesc;

public class DescWrapper {
    private AnalyzerContext analyzerContext_;

    private ClassDesc valueClassDesc_;

    private DescWrapper parent_;

    private PropertyDesc propertyDesc_;

    public DescWrapper(AnalyzerContext analyzerContext, ClassDesc valueClassDesc) {
        analyzerContext_ = analyzerContext;
        valueClassDesc_ = valueClassDesc;
    }

    public DescWrapper(DescWrapper parent, PropertyDesc propertyDesc) {
        analyzerContext_ = parent.getAnalizerContext();
        parent_ = parent;
        propertyDesc_ = propertyDesc;
    }

    public String toString() {
        return "0";
    }

    public Object get(String name) {
        if (!AnalyzerUtils.isValidAsSimplePropertyName(name)) {
            return null;
        }

        // 実際にプロパティを参照したこの時点でクラス定義を遅延決定するようにしている。
        if (propertyDesc_ != null) {
            analyzerContext_.preparePropertyTypeClassDesc(parent_
                    .getValueClassDesc(), propertyDesc_, true);
        }

        ClassDesc cd = getValueClassDesc();
        PropertyDesc pd = cd.getPropertyDesc(name);
        if (pd == null) {
            int mode = (cd.isKindOf(ClassDesc.KIND_DTO) ? (PropertyDesc.READ | PropertyDesc.WRITE)
                    : PropertyDesc.READ);
            pd = analyzerContext_.adjustPropertyType(cd.getName(), cd
                    .addProperty(name, mode));
        }

        TypeDesc td = pd.getTypeDesc();
        if (!td.isExplicit() && td.getName().equals("boolean")) {
            td.setClassDesc("String");
            pd.notifyUpdatingType();
        }

        DescWrapper returned = new DescWrapper(this, pd);
        if (pd.getTypeDesc().isArray()) {
            return new DescWrapper[] { returned };
        } else {
            return returned;
        }
    }

    public AnalyzerContext getAnalizerContext() {
        return analyzerContext_;
    }

    public PropertyDesc getPropertyDesc() {
        return propertyDesc_;
    }

    public ClassDesc getValueClassDesc() {
        if (valueClassDesc_ != null) {
            return valueClassDesc_;
        } else {
            return propertyDesc_.getTypeDesc().getClassDesc();
        }
    }

    public DescWrapper getParent() {
        return parent_;
    }
}
