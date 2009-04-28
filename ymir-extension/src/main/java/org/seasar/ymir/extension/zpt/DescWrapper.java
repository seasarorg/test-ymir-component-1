package org.seasar.ymir.extension.zpt;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassType;
import org.seasar.ymir.extension.creator.PropertyDesc;
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
        if (!AnalyzerUtils.isValidVariableName(name)) {
            return null;
        }

        ClassDesc cd = getValueClassDescToModifyProeprty(name);
        PropertyDesc pd = cd.getPropertyDesc(name);
        if (pd == null) {
            if (hasPresentSuperclassReadableProperty(cd, name)) {
                // 親クラスがプロパティを持っていてかつGetterが存在するので
                // プロパティを新規に作らないようにする。
                return null;
            }
        }

        int mode = (cd.isTypeOf(ClassType.DTO) ? (PropertyDesc.READ | PropertyDesc.WRITE)
                : PropertyDesc.READ);
        if (pd == null) {
            if (analyzerContext_.isOuter(cd)
                    && !analyzerContext_.hasProperty(cd.getName(), name)) {
                // 自動生成対象クラスではないのでプロパティを増やせない。プロパティがないためnullを返す。
                return null;
            }
            pd = analyzerContext_.addPropertyDesc(cd, name, mode);
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

    boolean hasPresentSuperclassReadableProperty(ClassDesc classDesc,
            String name) {
        Class<?> superclass = findPresentSuperclass(classDesc);
        if (superclass != null) {
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(superclass);
                for (PropertyDescriptor propertyDescriptor : beanInfo
                        .getPropertyDescriptors()) {
                    if (name.equals(propertyDescriptor.getName())
                            && propertyDescriptor.getReadMethod() != null) {
                        return true;
                    }
                }
            } catch (IntrospectionException ignore) {
            }
        }
        return false;
    }

    Class<?> findPresentSuperclass(ClassDesc classDesc) {
        ClassDesc cd = classDesc;
        String superclassName = null;
        Class<?> superclass = null;
        do {
            superclassName = cd.getSuperclassName();
            if (superclassName == null) {
                break;
            }

            superclass = analyzerContext_.getSourceCreator().getClass(
                    superclassName);
            if (superclass != null) {
                break;
            }

            cd = analyzerContext_.getSourceCreator().newClassDesc(null,
                    superclassName, null);
        } while (true);

        return superclass;
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
        if (propertyDesc_ != null && !propertyDesc_.getTypeDesc().isExplicit()) {
            propertyDesc_ = analyzerContext_.addPropertyDesc(
                    parent_.getValueClassDescToModifyProeprty(propertyDesc_
                            .getName()), propertyDesc_.getName(), propertyDesc_
                            .getMode(), variableName, asCollection,
                    collectionClassName, probability);
        }
    }
}
