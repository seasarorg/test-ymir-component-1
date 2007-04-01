package org.seasar.ymir.extension.creator;

public interface AnnotatedDesc {

    AnnotationDesc getAnnotationDesc(String name);

    void setAnnotationDesc(AnnotationDesc annotationDesc);

    AnnotationDesc[] getAnnotationDescs();
}
