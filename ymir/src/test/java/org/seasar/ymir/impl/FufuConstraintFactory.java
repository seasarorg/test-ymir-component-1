package org.seasar.ymir.impl;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ConstraintFactory;
import org.seasar.ymir.constraint.ConstraintFactoryBase;

public class FufuConstraintFactory extends ConstraintFactoryBase implements
        ConstraintFactory<Fufu> {

    public Constraint getConstraint(Fufu annotation, AnnotatedElement element) {

        return new FufuConstraint(annotation.value());
    }
}
