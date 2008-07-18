package org.seasar.ymir.extension.creator;

import java.util.Map;

public interface ClassDesc extends AnnotatedDesc, Cloneable {

    String KIND_PAGE = "Page";

    String KIND_DTO = "Dto";

    String KIND_DAO = "Dao";

    String KIND_DXO = "Dxo";

    String KIND_BEAN = "Bean";

    String KIND_CONVERTER = "Converter";

    Object clone();

    String getName();

    String getPackageName();

    String getShortName();

    String getInstanceName();

    String getKind();

    boolean isKindOf(String kind);

    String getBaseName();

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
