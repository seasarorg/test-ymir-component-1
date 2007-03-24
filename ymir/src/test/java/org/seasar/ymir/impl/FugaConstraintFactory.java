package org.seasar.ymir.extension.impl;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Constraint;
import org.seasar.ymir.extension.constraint.ConstraintFactory;
import org.seasar.ymir.extension.constraint.ConstraintFactoryBase;

public class FugaConstraintFactory extends ConstraintFactoryBase implements
        ConstraintFactory<Fuga> {

    public Constraint getConstraint(Fuga annotation, AnnotatedElement element) {

        return new FugaConstraint(annotation.value());
    }
}
