package org.seasar.ymir.extension.constraint;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Constraint;

public class TokenRequiredConstraintFactory extends ConstraintFactoryBase
        implements ConstraintFactory<TokenRequired> {

    public Constraint getConstraint(TokenRequired annotation,
            AnnotatedElement element) {

        return new TokenRequiredConstraint(annotation.value());
    }
}
