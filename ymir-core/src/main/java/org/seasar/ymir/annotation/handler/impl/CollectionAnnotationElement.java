package org.seasar.ymir.annotation.handler.impl;

import static org.seasar.ymir.annotation.handler.AnnotationElements.getPropertyNames;
import static org.seasar.ymir.annotation.handler.AnnotationElements.getPropertyValue;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.seasar.ymir.annotation.ForTesting;
import org.seasar.ymir.annotation.handler.AnnotationElement;
import org.seasar.ymir.annotation.handler.AnnotationElements;
import org.seasar.ymir.annotation.handler.AnnotationProcessor;

public class CollectionAnnotationElement extends AbstractAnnotationElement {
    private AnnotationElement[] expandedElements_;

    public CollectionAnnotationElement(Annotation annotation) {
        super(annotation);

        expand(getAnnotation());
    }

    @ForTesting
    CollectionAnnotationElement() {
        super(null);
    }

    void expand(Annotation collectionAnnotation) {
        List<AnnotationElement> list = new ArrayList<AnnotationElement>();
        for (String name : getPropertyNames(collectionAnnotation
                .annotationType())) {
            Object value = getPropertyValue(collectionAnnotation, name);
            Class<? extends Object> valueClass = value.getClass();
            boolean array = valueClass.isArray();
            Class<?> componentType = array ? valueClass.getComponentType()
                    : valueClass;
            if (!Annotation.class.isAssignableFrom(componentType)) {
                throw new IllegalArgumentException(
                        "Type of '"
                                + name
                                + "' property must be an instance or an array of annotation: "
                                + collectionAnnotation);
            }

            if (array) {
                for (Annotation anno : (Annotation[]) value) {
                    list.add(AnnotationElements.newInstance(anno));
                }
            } else {
                list.add(AnnotationElements.newInstance((Annotation) value));
            }
        }
        expandedElements_ = list.toArray(new AnnotationElement[0]);
    }

    @SuppressWarnings("unchecked")
    public <R> R accept(AnnotationProcessor<?> visitor, Object... parameters) {
        for (AnnotationElement element : expandedElements_) {
            R returned = (R) element.accept(visitor);
            if (returned != null) {
                return returned;
            }
        }
        return null;
    }

    @ForTesting
    AnnotationElement[] getExpandedElements() {
        return expandedElements_;
    }
}
