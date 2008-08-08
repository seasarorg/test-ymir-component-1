package org.seasar.ymir.annotation.handler.impl;

import static org.seasar.ymir.annotation.handler.AnnotationElements.getPropertyValue;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.seasar.ymir.ForTesting;
import org.seasar.ymir.annotation.handler.AnnotationElement;
import org.seasar.ymir.annotation.handler.AnnotationElements;
import org.seasar.ymir.annotation.handler.AnnotationProcessor;

public class CollectionAnnotationElement extends AbstractAnnotationElement {
    private static final String PROP_COLLECTION = "value";

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
        Annotation[] value;
        try {
            value = getPropertyValue(collectionAnnotation, PROP_COLLECTION);
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(
                    "Type of '"
                            + PROP_COLLECTION
                            + "' property must be an array of a subclass of Annotation: "
                            + collectionAnnotation, ex);
        }
        if (value == null) {
            throw new RuntimeException("Collection annotation must have '"
                    + PROP_COLLECTION + "' property: " + collectionAnnotation);
        }

        List<AnnotationElement> list = new ArrayList<AnnotationElement>();
        for (Annotation anno : value) {
            list.add(AnnotationElements.newInstance(anno));
        }
        expandedElements_ = list.toArray(new AnnotationElement[0]);
    }

    @SuppressWarnings("unchecked")
    public <R> R accept(AnnotationProcessor<?> visitor) {
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
