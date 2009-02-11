package org.seasar.ymir.extension.creator;

import java.util.HashMap;
import java.util.Map;

import org.seasar.ymir.extension.zpt.ParameterRole;

public class ClassHint {
    private String className_;

    private String superclassName_;

    private Map<String, ParameterRole> parameterRoleMap_ = new HashMap<String, ParameterRole>();

    public ClassHint(String className) {
        className_ = className;
    }

    @Override
    public String toString() {
        return "className=" + className_ + ", superclassName="
                + superclassName_ + ", parameterRoleMap=" + parameterRoleMap_;
    }

    public String getClassName() {
        return className_;
    }

    public String getSuperclassName() {
        return superclassName_;
    }

    public void setSuperclassName(String superclassName) {
        superclassName_ = superclassName;
    }

    public ParameterRole getParameterRole(String name) {
        ParameterRole role = parameterRoleMap_.get(name);
        if (role == null) {
            role = ParameterRole.UNDECIDED;
        }
        return role;
    }

    public void setParameterRole(String name, ParameterRole role) {
        parameterRoleMap_.put(name, role);
    }
}
