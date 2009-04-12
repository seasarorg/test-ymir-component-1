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

    void notifyTypeUpdated();

    boolean isTypeAlreadySet();

    AnnotationDesc getAnnotationDescOnGetter(String name);

    void setAnnotationDescOnGetter(AnnotationDesc annotationDesc);

    void removeMetaAnnotationDescOnGetter(String metaName);

    AnnotationDesc[] getAnnotationDescsOnGetter();

    void setAnnotationDescsOnGetter(AnnotationDesc[] annotationDescs);

    void removeMetaAnnotationDescOnSetter(String metaName);

    AnnotationDesc getAnnotationDescOnSetter(String name);

    void setAnnotationDescOnSetter(AnnotationDesc annotationDesc);

    AnnotationDesc[] getAnnotationDescsOnSetter();

    void setAnnotationDescsOnSetter(AnnotationDesc[] annotationDescs);

    void setAnnotationDescs(AnnotationDesc[] annotationDescs);

    String getMetaFirstValueOnSetter(String name);

    String getMetaFirstValueOnGetter(String name);

    boolean hasMetaOnGetter(String name);

    boolean hasMetaOnSetter(String name);

    String[] getMetaValueOnGetter(String name);

    String[] getMetaValueOnSetter(String name);

    Class<?>[] getMetaClassValueOnGetter(String name);

    Class<?>[] getMetaClassValueOnSetter(String name);

    MetaAnnotationDesc[] getMetaAnnotationDescsOnGetter();

    MetaAnnotationDesc[] getMetaAnnotationDescsOnSetter();

    String getInitialValue();
}
