package org.seasar.ymir.extension.creator;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.annotation.Metas;

public interface AnnotatedDesc {
    String ANNOTATION_NAME_META = Meta.class.getName();

    String ANNOTATION_NAME_METAS = Metas.class.getName();

    AnnotationDesc getAnnotationDesc(String name);

    void setAnnotationDesc(AnnotationDesc annotationDesc);

    AnnotationDesc[] getAnnotationDescs();

    String getMetaValue(String name);

    String[] getMetaValues(String name);

    Class<?>[] getMetaClassValues(String name);

    boolean hasMeta(String name);
}
