package org.seasar.ymir.extension.creator;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.annotation.Metas;

public interface PropertyDesc extends Cloneable {
    int NONE = 0;

    int READ = 1;

    int WRITE = 2;

    String ANNOTATION_NAME_META = Meta.class.getName();

    String ANNOTATION_NAME_METAS = Metas.class.getName();

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

    AnnotationDesc getAnnotationDesc(String name);

    void setAnnotationDesc(AnnotationDesc annotationDesc);

    AnnotationDesc[] getAnnotationDescs();

    void setAnnotationDescs(AnnotationDesc[] annotationDescs);

    boolean hasMetaOnGetter(String name);

    String getMetaValueOnGetter(String name);

    boolean hasMetaOnSetter(String name);

    String getMetaValueOnSetter(String name);

    boolean hasMeta(String name);

    String getMetaValue(String name);
}
