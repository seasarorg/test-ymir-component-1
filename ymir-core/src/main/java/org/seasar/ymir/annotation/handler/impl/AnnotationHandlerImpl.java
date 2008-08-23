package org.seasar.ymir.annotation.handler.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.annotation.handler.AnnotationElements;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.cache.CacheManager;

public class AnnotationHandlerImpl implements AnnotationHandler {
    private Map<Key, Boolean> presentMap_;

    private Map<Key, Annotation[]> annotationsMap_;

    private Map<Key, Annotation[]> markedAnnotationsMap_;

    @Binding(bindingType = BindingType.MUST)
    public void setCacheManager(CacheManager cacheManager) {
        presentMap_ = cacheManager.newMap();
        annotationsMap_ = cacheManager.newMap();
        markedAnnotationsMap_ = cacheManager.newMap();
    }

    public boolean isAnnotationPresent(AnnotatedElement element,
            Class<? extends Annotation> annotationClass) {
        Key key = new Key(element, annotationClass);
        Boolean present = presentMap_.get(key);
        if (present == null) {
            present = Boolean.valueOf(isAnnotationPresent0(element,
                    annotationClass));
            presentMap_.put(key, present);
        }
        return present.booleanValue();
    }

    protected boolean isAnnotationPresent0(AnnotatedElement element,
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
        Key key = new Key(element, annotationClass);
        Annotation[] annotations = annotationsMap_.get(key);
        if (annotations == null) {
            annotations = getAnnotations0(element, annotationClass);
            annotationsMap_.put(key, annotations);
        }
        return (T[]) annotations;
    }

    @SuppressWarnings("unchecked")
    protected <T extends Annotation> T[] getAnnotations0(
            AnnotatedElement element, Class<T> annotationClass) {
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
        Key key = new Key(element, metaAnnotationClass);
        Annotation[] metaAnnotations = markedAnnotationsMap_.get(key);
        if (metaAnnotations == null) {
            metaAnnotations = getMarkedAnnotations0(element,
                    metaAnnotationClass);
            markedAnnotationsMap_.put(key, metaAnnotations);
        }
        return metaAnnotations;
    }

    protected Annotation[] getMarkedAnnotations0(AnnotatedElement element,
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

    protected static class Key {
        private AnnotatedElement element_;

        private Class<? extends Annotation> annotationType_;

        public Key(AnnotatedElement element,
                Class<? extends Annotation> annotationType) {
            element_ = element;
            annotationType_ = annotationType;
        }

        @Override
        public int hashCode() {
            return element_.hashCode() + annotationType_.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != getClass()) {
                return false;
            }
            Key o = (Key) obj;
            return o.element_ == element_
                    && o.annotationType_ == annotationType_;
        }
    }
}
