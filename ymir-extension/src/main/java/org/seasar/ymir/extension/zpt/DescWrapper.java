package org.seasar.ymir.extension.zpt;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassType;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.impl.SimpleClassDesc;
import org.seasar.ymir.zpt.annotation.IgnoreException;

@IgnoreException(NoSuchMethodException.class)
public class DescWrapper {
    private AnalyzerContext analyzerContext_;

    private ClassDesc valueClassDesc_;

    private DescWrapper parent_;

    private PropertyDesc propertyDesc_;

    private String variableName_;

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
            pd = analyzerContext_.addProperty(cd, name, mode);
        } else {
            pd.addMode(mode);
        }

        TypeDesc td = pd.getTypeDesc();
        if (!td.isExplicit() && td.getName().equals("boolean")) {
            td
                    .setComponentClassDesc(new SimpleClassDesc(String.class
                            .getName()));
            pd.notifyTypeUpdated();
        }

        // [#YMIR-198] 仮にnameで指定されたプロパティの値がこの先使われなくても、オブジェクトはこのプロパティを持つ
        // はずなので、型情報などをセットしておく必要がある。
        cd.setPropertyDesc(pd);

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
            // このコードが実行されるケースには、今の値がなんらかのプロパティを
            // 持っていてそれを追加したいケースがあるが、
            // 今の値がbooleanやStringと間違って推論されていることもあるため、再度型推論を行なう。
            // cf. ZptAnalyzerTest#testAnalyze36()
            if (!propertyDesc_.getTypeDesc().isExplicit()) {
                propertyDesc_ = analyzerContext_.addProperty(parent_
                        .getValueClassDesc(), propertyDesc_.getName(),
                        propertyDesc_.getName(), propertyDesc_.getMode(), true);

            }
            return propertyDesc_.getTypeDesc().getComponentClassDesc();
        }
    }

    public DescWrapper getParent() {
        return parent_;
    }

    public void setVariableName(String variableName) {
        variableName_ = variableName;
        if (propertyDesc_ != null && !propertyDesc_.getTypeDesc().isExplicit()) {
            propertyDesc_ = analyzerContext_.addProperty(parent_
                    .getValueClassDesc(), propertyDesc_.getName(),
                    variableName_, propertyDesc_.getMode(), true);
        }
    }
}
