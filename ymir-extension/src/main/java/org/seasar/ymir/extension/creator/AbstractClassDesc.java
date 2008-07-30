package org.seasar.ymir.extension.creator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

abstract public class AbstractClassDesc extends AbstractAnnotatedDesc implements
        ClassDesc {
    private Map<String, Object> parameter_;

    abstract public String getName();

    public Object clone() {
        AbstractClassDesc cloned = (AbstractClassDesc) super.clone();

        if (parameter_ != null) {
            cloned.parameter_ = new HashMap<String, Object>();
            for (Iterator<Map.Entry<String, Object>> itr = parameter_
                    .entrySet().iterator(); itr.hasNext();) {
                Map.Entry<String, Object> entry = itr.next();
                cloned.parameter_.put(entry.getKey(), entry.getValue());
            }
        }
        return cloned;
    }

    public ClassType getType() {
        return getType(getName());
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

        return getShortName(getName());
    }

    String getShortName(String name) {

        int dot = name.lastIndexOf('.');
        if (dot < 0) {
            return name;
        } else {
            return name.substring(dot + 1);
        }
    }

    public String getNameBase() {
        return getNameBase(getName());
    }

    String getNameBase(String name) {
        name = getShortName(name);
        for (ClassType type : ClassType.values()) {
            if (name.endsWith(type.getSuffix())) {
                return name.substring(0, name.length()
                        - type.getSuffix().length());
            }
        }
        return name;
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

        return uncapFirst(getShortName(name));
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
}
