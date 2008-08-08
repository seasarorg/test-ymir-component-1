package org.seasar.ymir.annotation.handler.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;

import org.seasar.ymir.annotation.handler.AnnotationElements;
import org.seasar.ymir.annotation.handler.AnnotationHandler;

public class AnnotationHandlerImpl implements AnnotationHandler {
    public boolean isAnnotationPresent(AnnotatedElement element,
            Class<? extends Annotation> annotationClass) {
        if (element == null) {
            return false;
        }

        AnnotationExistenceChecker checker = new AnnotationExistenceChecker(
                annotationClass);
        for (Annotation annotation : element.getAnnotations()) {
            if (AnnotationElements.newInstance(annotation).accept(checker) != null) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public <T extends Annotation> T[] getAnnotations(AnnotatedElement element,
            Class<T> annotationClass) {
        if (element == null) {
            return (T[]) Array.newInstance(annotationClass, 0);
        }

        AnnotationGatherer gatherer = new AnnotationGatherer(annotationClass);
        for (Annotation annotation : element.getAnnotations()) {
            AnnotationElements.newInstance(annotation).accept(gatherer);
        }
        return (T[]) gatherer.getAnnotations();
    }

    public <T extends Annotation> T getAnnotation(AnnotatedElement element,
            Class<T> annotationClass) {
        T[] annotations = getAnnotations(element, annotationClass);
        if (annotations.length == 0) {
            return null;
        } else if (annotations.length == 1) {
            return annotations[0];
        } else {
            throw new IllegalStateException(
                    "Multiple annotations found: element=" + element
                            + ", annotationClass=" + annotationClass);
        }
    }

    public Annotation[] getMarkedAnnotations(AnnotatedElement element,
            Class<? extends Annotation> metaAnnotationClass) {
        if (element == null) {
            return new Annotation[0];
        }

        MarkedAnnotationGatherer gatherer = new MarkedAnnotationGatherer(
                metaAnnotationClass);
        for (Annotation annotation : element.getAnnotations()) {
            AnnotationElements.newInstance(annotation).accept(gatherer);
        }
        return gatherer.getAnnotations();
    }
}
