package org.seasar.ymir.extension.creator;

public interface ParameterDesc extends Cloneable {

    Object clone();

    TypeDesc getTypeDesc();

    void setTypeDesc(TypeDesc typeDesc);

    String getName();

    String getNameAsIs();

    void setName(String name);
}
