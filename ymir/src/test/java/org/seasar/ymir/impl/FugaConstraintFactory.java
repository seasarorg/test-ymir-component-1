package org.seasar.ymir.impl;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ConstraintFactory;
import org.seasar.ymir.constraint.ConstraintFactoryBase;

public class FugaConstraintFactory extends ConstraintFactoryBase implements
        ConstraintFactory<Fuga> {

    public Constraint getConstraint(Fuga annotation, AnnotatedElement element) {

        return new FugaConstraint(annotation.value());
    }
}
