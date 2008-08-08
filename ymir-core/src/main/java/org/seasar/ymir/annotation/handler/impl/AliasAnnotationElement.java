package org.seasar.ymir.annotation.handler.impl;

import static org.seasar.ymir.annotation.handler.AnnotationElements.getPropertyValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.seasar.ymir.ForTesting;
import org.seasar.ymir.annotation.handler.AnnotationElement;
import org.seasar.ymir.annotation.handler.AnnotationElements;
import org.seasar.ymir.annotation.handler.AnnotationProcessor;

public class AliasAnnotationElement extends AbstractAnnotationElement {
    private static final String PROP_ALIAS = "z_alias";

    private static final String PROP_ANNOTATIONTYPE = "annotationType";

    private AnnotationElement expandedElement_;

    public AliasAnnotationElement(Annotation annotation) {
        super(annotation);

        expand(getAnnotation());
    }

    @ForTesting
    AliasAnnotationElement() {
        super(null);
    }

    void expand(final Annotation aliasAnnotation) {
        final Annotation originalAnnotation;
        try {
            originalAnnotation = getPropertyValue(aliasAnnotation, PROP_ALIAS);
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("Type of '" + PROP_ALIAS
                    + "' property must be a subclass of Annotation: "
                    + aliasAnnotation, ex);
        }
        if (originalAnnotation == null) {
            throw new RuntimeException("Alias annotation must have '"
                    + PROP_ALIAS + "' property: " + aliasAnnotation);
        }

        expandedElement_ = AnnotationElements.newInstance((Annotation) Proxy
                .newProxyInstance(originalAnnotation.annotationType()
                        .getClassLoader(), new Class[] { originalAnnotation
                        .annotationType() }, new InvocationHandler() {
                    public Object invoke(Object proxy, Method method,
                            Object[] args) throws Throwable {
                        if (method.getDeclaringClass() == Object.class) {
                            return method.invoke(originalAnnotation, args);
                        }
                        String name = method.getName();
                        if (name.equals(PROP_ANNOTATIONTYPE)) {
                            return originalAnnotation.annotationType();
                        } else if (name.equals(PROP_ALIAS)) {
                            return getPropertyValue(originalAnnotation, name);
                        }
                        Object value = getPropertyValue(aliasAnnotation, name);
                        if (value == null) {
                            value = getPropertyValue(originalAnnotation, name);
                        }
                        return value;
                    }
                }));
    }

    @SuppressWarnings("unchecked")
    public <R> R accept(AnnotationProcessor<?> visitor) {
        return (R) expandedElement_.accept(visitor);
    }

    @ForTesting
    AnnotationElement getExpandedElement() {
        return expandedElement_;
    }
}
