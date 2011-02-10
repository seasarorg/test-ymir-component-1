package org.seasar.ymir.constraint;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.impl.AbstractCrosscuttingConstraint;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;

public class HoeCrosscuttingConstraint extends AbstractCrosscuttingConstraint {
    public ConstraintType getConstraintType() {
        return ConstraintType.VALIDATION;
    }

    public void confirm(Object component, Request request)
            throws PermissionDeniedException, ValidationFailedException {
        throw new ValidationFailedException(new Notes().add(new Note("test")));
    }
}
