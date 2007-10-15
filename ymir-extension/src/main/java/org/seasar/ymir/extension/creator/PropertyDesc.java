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

    boolean isWritable();

    void notifyUpdatingType();

    boolean isTypeAlreadySet();

    AnnotationDesc getAnnotationDescForGetter(String name);

    AnnotationDesc[] getAnnotationDescsForGetter();

    void setAnnotationDescForGetter(AnnotationDesc annotationDesc);

    AnnotationDesc getAnnotationDescForSetter(String name);

    AnnotationDesc[] getAnnotationDescsForSetter();

    void setAnnotationDescForSetter(AnnotationDesc annotationDesc);

    AnnotationDesc getAnnotationDesc(String name);

    AnnotationDesc[] getAnnotationDescs();

    void setAnnotationDesc(AnnotationDesc annotationDesc);

    boolean hasMetaOnGetter(String name);

    String getMetaValueOnGetter(String name);

    boolean hasMetaOnSetter(String name);

    String getMetaValueOnSetter(String name);

    boolean hasMeta(String name);

    String getMetaValue(String name);
}
