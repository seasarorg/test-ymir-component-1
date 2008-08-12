package org.seasar.ymir.extension.creator;

public class ClassHint {
    private String className_;

    private String superclassName_;

    public ClassHint(String className, String superclassName) {
        className_ = className;
        superclassName_ = superclassName;
    }

    @Override
    public String toString() {
        return "className=" + className_ + ", superclassName="
                + superclassName_;
    }

    public String getClassName() {
        return className_;
    }

    public String getSuperclassName() {
        return superclassName_;
    }
}
