package org.seasar.ymir.extension.creator;

public interface PropertyDesc extends AnnotatedDesc, Cloneable {
    int NONE = 0;

    int READ = 1;

    int WRITE = 2;

    Object clone();

    String getName();

    TypeDesc getTypeDesc();

    void setTypeDesc(TypeDesc typeDesc);

    void setTypeDesc(String typeName);

    void setTypeDesc(String typeName, boolean explicit);

    int getMode();

    void setMode(int mode);

    void addMode(int mode);

    boolean isReadable();

    String getGetterName();

    void setGetterName(String getterName);

    boolean isWritable();

    void notifyUpdatingType();

    boolean isTypeAlreadySet();

    AnnotationDesc getAnnotationDescForGetter(String name);

    void setAnnotationDescForGetter(AnnotationDesc annotationDesc);

    AnnotationDesc[] getAnnotationDescsForGetter();

    void setAnnotationDescsForGetter(AnnotationDesc[] annotationDescs);

    AnnotationDesc getAnnotationDescForSetter(String name);

    void setAnnotationDescForSetter(AnnotationDesc annotationDesc);

    AnnotationDesc[] getAnnotationDescsForSetter();

    void setAnnotationDescsForSetter(AnnotationDesc[] annotationDescs);

    void setAnnotationDescs(AnnotationDesc[] annotationDescs);

    boolean hasMetaOnGetter(String name);

    String getMetaFirstValueOnGetter(String name);

    boolean hasMetaOnSetter(String name);

    String getMetaFirstValueOnSetter(String name);
}
