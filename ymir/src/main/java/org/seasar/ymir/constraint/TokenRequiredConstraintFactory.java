package org.seasar.ymir.constraint;

import java.lang.reflect.AnnotatedElement;

public class TokenRequiredConstraintFactory extends ConstraintFactoryBase
        implements ConstraintFactory<TokenRequired> {

    public Constraint getConstraint(TokenRequired annotation,
            AnnotatedElement element) {

        return new TokenRequiredConstraint(annotation.value());
    }
}
