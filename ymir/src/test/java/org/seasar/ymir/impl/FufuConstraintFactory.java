package org.seasar.ymir.extension.impl;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Constraint;
import org.seasar.ymir.extension.constraint.ConstraintFactory;
import org.seasar.ymir.extension.constraint.ConstraintFactoryBase;

public class FufuConstraintFactory extends ConstraintFactoryBase implements
        ConstraintFactory<Fufu> {

    public Constraint getConstraint(Fufu annotation, AnnotatedElement element) {

        return new FufuConstraint(annotation.value());
    }
}
