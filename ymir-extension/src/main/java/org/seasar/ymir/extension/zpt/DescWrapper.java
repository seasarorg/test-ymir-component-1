package org.seasar.ymir.extension.zpt;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassType;
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
        if (!AnalyzerUtils.isValidVariableName(name)) {
            return null;
        }

        ClassDesc cd = getValueClassDesc();
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
            pd = analyzerContext_.adjustPropertyType(cd.getName(), cd
                    .addProperty(name, mode));
        } else {
            pd.addMode(mode);
        }

        TypeDesc td = pd.getTypeDesc();
        if (!td.isExplicit() && td.getName().equals("boolean")) {
            td.setClassDesc("String");
            pd.notifyUpdatingType();
        }

        // [#YMIR-198] 仮にnameで指定されたプロパティの値がこの先使われなくても、オブジェクトはこのプロパティを持つ
        // はずなので、型情報などをセットしておく必要がある。
        cd.setPropertyDesc(pd);

        DescWrapper returned = new DescWrapper(this, pd);
        if (pd.getTypeDesc().isArray()) {
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

            cd = analyzerContext_.getSourceCreator().newClassDesc(
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

    public ClassDesc getValueClassDesc() {
        if (valueClassDesc_ != null) {
            return valueClassDesc_;
        } else {
            // 実際にプロパティを参照したこの時点でクラス定義を遅延決定するようにしている。
            analyzerContext_.preparePropertyTypeClassDesc(parent_
                    .getValueClassDesc(), propertyDesc_, true);

            return propertyDesc_.getTypeDesc().getClassDesc();
        }
    }

    public DescWrapper getParent() {
        return parent_;
    }
}
