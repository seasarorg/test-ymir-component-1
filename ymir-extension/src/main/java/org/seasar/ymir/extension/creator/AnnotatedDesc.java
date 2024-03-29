package org.seasar.ymir.extension.creator;

import java.util.Map;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.annotation.Metas;

public interface AnnotatedDesc<T extends AnnotatedDesc<?>> extends Desc<T> {
    String ANNOTATION_NAME_META = Meta.class.getName();

    String ANNOTATION_NAME_METAS = Metas.class.getName();

    AnnotationDesc getAnnotationDesc(String name);

    void setAnnotationDesc(AnnotationDesc annotationDesc);

    void setAnnotationDescs(AnnotationDesc... annotationDescs);

    void removeMetaAnnotationDesc(String metaName);

    AnnotationDesc[] getAnnotationDescs();

    String getMetaFirstValue(String name);

    String[] getMetaValue(String name);

    Class<?>[] getMetaClassValue(String name);

    boolean hasMeta(String name);

    MetaAnnotationDesc[] getMetaAnnotationDescs();

    Object getAttribute(String name);

    void setAttribute(String name, Object value);

    void removeAttribute(String name);

    Map<String, Object> getAttributeMap();

    void setAttributeMap(Map<String, Object> attributeMap);
}
