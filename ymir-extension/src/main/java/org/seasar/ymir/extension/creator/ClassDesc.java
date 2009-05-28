package org.seasar.ymir.extension.creator;

import java.util.Map;

public interface ClassDesc extends AnnotatedDesc<ClassDesc> {
    String getName();

    String getQualifier();

    String getPackageName();

    String getShortName();

    String getInstanceName();

    ClassType getType();

    boolean isTypeOf(ClassType type);

    TypeDesc[] getInterfaceTypeDescs();

    void setInterfaceTypeDescs(TypeDesc... interfaceTypeDescs);

    String getNameBase();

    MethodDesc[] getMethodDescs(String name);

    MethodDesc getMethodDesc(MethodDesc methodDesc);

    MethodDesc[] getMethodDescs();

    MethodDesc[] getMethodDescsOrderByName();

    PropertyDesc getPropertyDesc(String name);

    PropertyDesc[] getPropertyDescs();

    PropertyDesc[] getPropertyDescsOrderByName();

    void setPropertyDesc(PropertyDesc propertyDesc);

    void removePropertyDesc(String name);

    PropertyDesc addPropertyDesc(String name, int mode);

    /**
     * 指定されたClassDescの内容をこのClassDescにマージします。
     * <p><code>force</code>がfalseの場合、
     * 重複している内容についてはもともとある方が優先されます。
     * <code>force</code>がtrueの場合、
     * 重複している内容については<code>classDesc</code>にある方が優先されます。
     * </p>
     *
     * @param classDesc マージするClassDesc。nullを指定した場合は何もしません。
     * @param force 引数で与えたClassDescの内容を優先するかどうか。
     */
    void merge(ClassDesc classDesc, boolean force);

    void setMethodDesc(MethodDesc methodDesc);

    void removeMethodDesc(MethodDesc methodDesc);

    String getSuperclassName();

    void setSuperclassName(String superclassName);

    String getSuperclassShortName();

    boolean isAbstract();

    void setAbstract(boolean baseClassAbstract);

    boolean isEmpty();

    void clear();

    Map<String, Object> getSourceGeneratorParameter();

    void setSourceGeneratorParameter(Map<String, Object> parameter);

    void setPropertyDescs(PropertyDesc... propertyDescs);

    void setMethodDescs(MethodDesc... methodDescs);

    String getBornOf();

    void setBornOf(String path);

    void removeBornOf(String bornOf);

    String getPathOfClass();

    String toShortName(String className);
}
