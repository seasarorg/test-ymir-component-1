package org.seasar.ymir.extension.creator;

import java.lang.reflect.Type;

public interface ParameterDesc extends Cloneable {
    Object clone();

    DescPool getDescPool();

    TypeDesc getTypeDesc();

    void setTypeDesc(TypeDesc typeDesc);

    void setTypeDesc(Type type);

    void setTypeDesc(String typeName);

    String getName();

    String getNameAsIs();

    void setName(String name);
}
