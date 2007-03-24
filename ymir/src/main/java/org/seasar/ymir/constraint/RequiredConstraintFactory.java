package org.seasar.ymir.constraint;

import java.lang.reflect.AnnotatedElement;

public class RequiredConstraintFactory extends ConstraintFactoryBase implements
        ConstraintFactory<Required> {

    public Constraint getConstraint(Required annotation,
            AnnotatedElement element) {

        String[] names = add(annotation.value(), getPropertyName(element));
        if (names.length == 0) {
            throw new IllegalArgumentException(
                    "Please specify at least one property: " + element);
        }
        return new RequiredConstraint(names);
    }
}
