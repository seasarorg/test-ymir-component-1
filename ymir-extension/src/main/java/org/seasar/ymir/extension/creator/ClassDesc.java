package org.seasar.ymir.extension.creator;

import java.util.Map;

public interface ClassDesc extends AnnotatedDesc, Cloneable {
    Object clone();

    String getName();

    String getPackageName();

    String getShortName();

    String getInstanceName();

    ClassType getType();

    boolean isTypeOf(ClassType type);

    String getNameBase();

    MethodDesc getMethodDesc(MethodDesc methodDesc);

    MethodDesc[] getMethodDescs();

    PropertyDesc getPropertyDesc(String name);

    PropertyDesc[] getPropertyDescs();

    void setPropertyDesc(PropertyDesc propertyDesc);

    void removePropertyDesc(String name);

    PropertyDesc addProperty(String name, int mode);

    /**
     * 指定されたClassDescの内容をこのClassDescにマージします。
     *
     * @param classDesc マージするClassDesc。nullを指定した場合は何もしません。
     */
    void merge(ClassDesc classDesc);

    void setName(String name);

    void setMethodDesc(MethodDesc methodDesc);

    void removeMethodDesc(MethodDesc methodDesc);

    void setSuperclass(Class<?> superclass);

    String getSuperclassName();

    Class<?> getSuperclass();

    boolean isBaseClassAbstract();

    void setBaseClassAbstract(boolean baseClassAbstract);

    boolean isEmpty();

    void clear();

    Map<String, Object> getOptionalSourceGeneratorParameter();

    void setOptionalSourceGeneratorParameter(Map<String, Object> parameter);
}
