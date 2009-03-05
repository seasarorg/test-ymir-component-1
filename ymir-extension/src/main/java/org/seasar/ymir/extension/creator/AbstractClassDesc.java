package org.seasar.ymir.extension.creator;

import java.util.HashMap;
import java.util.Map;

import org.seasar.ymir.util.ClassUtils;

abstract public class AbstractClassDesc extends AbstractAnnotatedDesc implements
        ClassDesc {
    private Map<String, Object> parameter_;

    private ClassType type_;

    private String bornOf_;

    abstract public String getName();

    public Object clone() {
        AbstractClassDesc cloned = (AbstractClassDesc) super.clone();

        if (parameter_ != null) {
            cloned.parameter_ = new HashMap<String, Object>(parameter_);
        }
        return cloned;
    }

    public ClassType getType() {
        if (type_ != null) {
            return type_;
        } else {
            return getType(getName());
        }
    }

    public void setType(ClassType type) {
        type_ = type;
    }

    ClassType getType(String name) {
        for (ClassType type : ClassType.values()) {
            if (name.endsWith(type.getSuffix())) {
                return type;
            }
        }
        return ClassType.BEAN;
    }

    public boolean isTypeOf(ClassType type) {
        return type == getType();
    }

    public String toString() {
        return getName();
    }

    public String getShortName() {
        return ClassUtils.getShortName(getName());
    }

    public String getNameBase() {
        return getNameBase(getName());
    }

    String getNameBase(String name) {
        name = ClassUtils.getShortName(name);
        return name
                .substring(0, name.length() - getType().getSuffix().length());
    }

    public String getPackageName() {
        return getPackageName(getName());
    }

    String getPackageName(String name) {
        int dot = name.lastIndexOf('.');
        if (dot < 0) {
            return "";
        } else {
            return name.substring(0, dot);
        }
    }

    public String getInstanceName() {
        return getInstanceName(getName());
    }

    String getInstanceName(String name) {
        String instanceName = uncapFirst(ClassUtils.getShorterName(name));
        if (instanceName.equals(name)) {
            instanceName += "Value";
        }
        return instanceName;
    }

    String uncapFirst(String string) {
        if (string == null || string.length() == 0) {
            return string;
        } else {
            return Character.toLowerCase(string.charAt(0))
                    + string.substring(1);
        }
    }

    public Map<String, Object> getOptionalSourceGeneratorParameter() {
        return parameter_;
    }

    public void setOptionalSourceGeneratorParameter(
            Map<String, Object> parameter) {
        parameter_ = parameter;
    }

    public void clear() {
        super.clear();
        parameter_ = null;
    }

    public String getBornOf() {
        return bornOf_;
    }

    public void setBornOf(String bornOf) {
        bornOf_ = bornOf;
    }
}
