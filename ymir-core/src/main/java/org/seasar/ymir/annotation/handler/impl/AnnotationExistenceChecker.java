package org.seasar.ymir.annotation.handler.impl;

import java.lang.annotation.Annotation;

import org.seasar.ymir.annotation.handler.AnnotationElement;
import org.seasar.ymir.annotation.handler.AnnotationProcessor;

public class AnnotationExistenceChecker implements AnnotationProcessor<Boolean> {
    private Class<? extends Annotation> annotationType_;

    public AnnotationExistenceChecker(Class<? extends Annotation> annotationType) {
        annotationType_ = annotationType;
    }

    public Boolean visit(AnnotationElement acceptor, Object... parameters) {
        if (acceptor.getAnnotation().annotationType().equals(annotationType_)) {
            return Boolean.TRUE;
        } else {
            return null;
        }
    }
}
