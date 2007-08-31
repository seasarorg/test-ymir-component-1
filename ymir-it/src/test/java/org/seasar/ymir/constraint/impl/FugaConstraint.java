package org.seasar.ymir.constraint.impl;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Note;
import org.seasar.ymir.Notes;
import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Fuga;

public class FugaConstraint implements Constraint<Fuga> {
    public void confirm(Object component, Request request, Fuga annotation,
            AnnotatedElement element) throws ConstraintViolatedException {
        throw new ValidationFailedException(new Notes().add(annotation.value(),
                new Note("")));
    }
}
