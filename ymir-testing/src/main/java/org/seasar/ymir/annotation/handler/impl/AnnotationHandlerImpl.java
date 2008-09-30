package org.seasar.ymir.annotation.handler.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
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
        if (element == null) {
            return false;
        }

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
        return getAnnotations0(element.getAnnotations(), annotationClass);
    }

    @SuppressWarnings("unchecked")
    protected <T extends Annotation> T[] getAnnotations0(
            Annotation[] annotations, Class<T> annotationClass) {
        AnnotationGatherer gatherer = new AnnotationGatherer(annotationClass);
        for (Annotation annotation : annotations) {
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

        Key key = new Key(element, metaAnnotationClass);
        Annotation[] markedAnnotations = markedAnnotationsMap_.get(key);
        if (markedAnnotations == null) {
            markedAnnotations = getMarkedAnnotations0(element,
                    metaAnnotationClass);
            markedAnnotationsMap_.put(key, markedAnnotations);
        }
        return markedAnnotations;
    }

    protected Annotation[] getMarkedAnnotations0(AnnotatedElement element,
            Class<? extends Annotation> metaAnnotationClass) {
        return getMarkedAnnotations0(element.getAnnotations(),
                metaAnnotationClass);
    }

    protected Annotation[] getMarkedAnnotations0(Annotation[] annotations,
            Class<? extends Annotation> metaAnnotationClass) {
        MarkedAnnotationGatherer gatherer = new MarkedAnnotationGatherer(
                metaAnnotationClass);
        for (Annotation annotation : annotations) {
            AnnotationElements.newInstance(annotation).accept(gatherer);
        }
        return gatherer.getAnnotations();
    }

    @SuppressWarnings("unchecked")
    public <T extends Annotation> T[] getParameterAnnotations(Method method,
            int index, Class<T> annotationClass) {
        if (method == null) {
            return (T[]) Array.newInstance(annotationClass, 0);
        }

        Key key = new Key(method, index, annotationClass);
        Annotation[] annotations = annotationsMap_.get(key);
        if (annotations == null) {
            annotations = getAnnotations0(
                    method.getParameterAnnotations()[index], annotationClass);
            annotationsMap_.put(key, annotations);
        }
        return (T[]) annotations;
    }

    public Annotation[] getMarkedParameterAnnotations(Method method, int index,
            Class<? extends Annotation> metaAnnotationClass) {
        if (method == null) {
            return new Annotation[0];
        }

        Key key = new Key(method, index, metaAnnotationClass);
        Annotation[] markedAnnotations = markedAnnotationsMap_.get(key);
        if (markedAnnotations == null) {
            markedAnnotations = getMarkedAnnotations0(method
                    .getParameterAnnotations()[index], metaAnnotationClass);
            markedAnnotationsMap_.put(key, markedAnnotations);
        }
        return markedAnnotations;
    }

    protected static class Key {
        private AnnotatedElement element_;

        private int index_;

        private Class<? extends Annotation> annotationType_;

        public Key(AnnotatedElement element,
                Class<? extends Annotation> annotationType) {
            this(element, -1, annotationType);
        }

        public Key(AnnotatedElement element, int index,
                Class<? extends Annotation> annotationType) {
            element_ = element;
            index_ = index;
            annotationType_ = annotationType;
        }

        @Override
        public int hashCode() {
            return element_.hashCode() + index_ + annotationType_.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != getClass()) {
                return false;
            }
            Key o = (Key) obj;
            return o.element_ == element_ && o.index_ == index_
                    && o.annotationType_ == annotationType_;
        }

        @Override
        public String toString() {
            return "(" + element_ + ", " + index_ + ", " + annotationType_
                    + ")";
        }
    }
}
