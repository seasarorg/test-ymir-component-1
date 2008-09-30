package org.seasar.ymir.annotation.handler;

import java.lang.annotation.Annotation;

import org.seasar.ymir.Acceptor;

public interface AnnotationElement extends Acceptor<AnnotationProcessor<?>> {
    Annotation getAnnotation();

    Class<? extends Annotation> getType();
}
