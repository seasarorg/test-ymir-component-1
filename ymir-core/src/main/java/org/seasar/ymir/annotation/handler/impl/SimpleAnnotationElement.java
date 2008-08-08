package org.seasar.ymir.annotation.handler.impl;

import java.lang.annotation.Annotation;

import org.seasar.ymir.annotation.handler.AnnotationProcessor;

public class SimpleAnnotationElement extends AbstractAnnotationElement {
    public SimpleAnnotationElement(Annotation annotation) {
        super(annotation);
    }

    @SuppressWarnings("unchecked")
    public <R> R accept(AnnotationProcessor<?> visitor) {
        return (R) visitor.visit(this);
    }
}
