package org.seasar.ymir.annotation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.seasar.ymir.annotation.Alias;
import org.seasar.ymir.annotation.Collection;
import org.seasar.ymir.annotation.handler.impl.AliasAnnotationElement;
import org.seasar.ymir.annotation.handler.impl.CollectionAnnotationElement;
import org.seasar.ymir.annotation.handler.impl.SimpleAnnotationElement;

public class AnnotationElements {
    private AnnotationElements() {
    }

    public static AnnotationElement newInstance(Annotation annotation) {
        Class<? extends Annotation> annotationType = annotation
                .annotationType();
        if (annotationType.isAnnotationPresent(Alias.class)) {
            return new AliasAnnotationElement(annotation);
        } else if (annotationType.isAnnotationPresent(Collection.class)) {
            return new CollectionAnnotationElement(annotation);
        } else {
            return new SimpleAnnotationElement(annotation);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getPropertyValue(Annotation annotation, String name) {
        Method method;
        try {
            method = annotation.annotationType().getMethod(name, new Class[0]);
        } catch (NoSuchMethodException ex) {
            return null;
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }
        try {
            return (T) method.invoke(annotation, new Object[0]);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex.getTargetException());
        }
    }
}
