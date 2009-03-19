package org.seasar.ymir.annotation.handler.impl;

import static org.seasar.ymir.annotation.handler.AnnotationElements.getPropertyValue;
import static org.seasar.ymir.annotation.handler.impl.AliasAnnotationElement.PROP_ALIAS;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.annotation.ForTesting;
import org.seasar.ymir.annotation.handler.AnnotationElement;
import org.seasar.ymir.annotation.handler.AnnotationElements;
import org.seasar.ymir.annotation.handler.AnnotationProcessor;

public class AliasAnnotationElement extends AbstractAnnotationElement {
    public static final String PROP_ALIAS = "z_alias";

    private AnnotationElement expandedElement_;

    public AliasAnnotationElement(Annotation annotation) {
        super(annotation);

        expand(getAnnotation());
    }

    @ForTesting
    AliasAnnotationElement() {
        super(null);
    }

    void expand(Annotation aliasAnnotation) {
        Annotation originalAnnotation;
        try {
            originalAnnotation = getPropertyValue(aliasAnnotation, PROP_ALIAS);
        } catch (ClassCastException ex) {
            throw new IllegalClientCodeRuntimeException("Type of '"
                    + PROP_ALIAS + "' element must be an Annotation: "
                    + aliasAnnotation.annotationType().getName(), ex);
        }
        if (originalAnnotation == null) {
            throw new IllegalClientCodeRuntimeException(
                    "@Alias annotation must have '" + PROP_ALIAS
                            + "' element: "
                            + aliasAnnotation.annotationType().getName());
        }

        expandedElement_ = AnnotationElements.newInstance((Annotation) Proxy
                .newProxyInstance(originalAnnotation.annotationType()
                        .getClassLoader(), new Class[] { originalAnnotation
                        .annotationType() },
                        new AliasAnnotationInvocationHandler(aliasAnnotation,
                                originalAnnotation)));
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
