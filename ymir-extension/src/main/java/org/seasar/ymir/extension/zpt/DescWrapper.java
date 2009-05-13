package org.seasar.ymir.extension.zpt;

import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassType;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.zpt.annotation.IgnoreException;

@IgnoreException(NoSuchMethodException.class)
public class DescWrapper {
    private AnalyzerContext analyzerContext_;

    private ClassDesc valueClassDesc_;

    private DescWrapper parent_;

    private PropertyDesc propertyDesc_;

    public DescWrapper(AnalyzerContext analyzerContext, ClassDesc valueClassDesc) {
        analyzerContext_ = analyzerContext;
        valueClassDesc_ = valueClassDesc;
    }

    private DescWrapper(DescWrapper parent, PropertyDesc propertyDesc) {
        analyzerContext_ = parent.getAnalizerContext();
        parent_ = parent;
        propertyDesc_ = propertyDesc;
    }

    public String toString() {
        return "0";
    }

    public Object get(String name) {
        if ("%value".equals(name)) {
            // %valueがついたプロパティはNoteとみなす。
            if (propertyDesc_ != null
                    && !propertyDesc_
                            .isTypeAlreadySet(SourceCreator.PROBABILITY_TYPE)) {
                propertyDesc_.setTypeDesc(Note.class);
                propertyDesc_.notifyTypeUpdated(SourceCreator.PROBABILITY_TYPE);
            }
            return null;
        }
        if (!AnalyzerUtils.isValidVariableName(name)) {
            return null;
        }

        ClassDesc cd = getValueClassDescToModifyProeprty(name);
        PropertyDesc pd = cd.getPropertyDesc(name);
        int mode = (cd.isTypeOf(ClassType.DTO) ? (PropertyDesc.READ | PropertyDesc.WRITE)
                : PropertyDesc.READ);
        if (pd == null) {
            if (analyzerContext_.getSourceCreator().isOuter(cd)
                    && !analyzerContext_.hasProperty(cd.getName(), name)) {
                // 自動生成対象クラスではないのでプロパティを増やせない。プロパティがないためnullを返す。
                return null;
            }
            pd = analyzerContext_.getSourceCreator().addPropertyDesc(cd, name,
                    mode, analyzerContext_.getPageClassName());
        } else {
            pd.addMode(mode);
        }

        pd.incrementReferCount();

        DescWrapper returned = new DescWrapper(this, pd);
        if (pd.getTypeDesc().isCollection()) {
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

    public ClassDesc getValueClassDescToModifyProeprty(String propertyName) {
        if (valueClassDesc_ != null) {
            return valueClassDesc_;
        } else {
            analyzerContext_.replaceTypeToGeneratedClassIfNeedToAddProperty(
                    propertyDesc_, propertyName, parent_.getValueClassDesc());
            return propertyDesc_.getTypeDesc().getComponentClassDesc();
        }
    }

    public ClassDesc getValueClassDesc() {
        if (valueClassDesc_ != null) {
            return valueClassDesc_;
        } else {
            return propertyDesc_.getTypeDesc().getComponentClassDesc();
        }
    }

    public DescWrapper getParent() {
        return parent_;
    }

    public void setVariableName(String variableName, boolean asCollection,
            String collectionClassName, int probability) {
        if (propertyDesc_ != null
                && !propertyDesc_
                        .isTypeAlreadySet(PropertyDesc.PROBABILITY_MAXIMUM)) {
            propertyDesc_ = analyzerContext_
                    .getSourceCreator()
                    .addPropertyDesc(
                            parent_
                                    .getValueClassDescToModifyProeprty(propertyDesc_
                                            .getName()),
                            propertyDesc_.getName(), propertyDesc_.getMode(),
                            variableName, asCollection, collectionClassName,
                            probability, analyzerContext_.getPageClassName());
        }
    }
}
