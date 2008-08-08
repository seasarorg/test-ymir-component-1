package org.seasar.ymir.annotation.handler.impl;

import java.lang.annotation.Annotation;

import org.seasar.ymir.annotation.handler.AnnotationElement;

abstract public class AbstractAnnotationElement implements AnnotationElement {
    private Annotation annotation_;

    protected AbstractAnnotationElement(Annotation annotation) {
        annotation_ = annotation;
    }

    public Annotation getAnnotation() {
        return annotation_;
    }

    public Class<? extends Annotation> getType() {
        return annotation_.annotationType();
    }
}
