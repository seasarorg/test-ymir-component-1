package org.seasar.ymir.constraint.impl;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Fufu;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;

public class FufuConstraint implements Constraint<Fufu> {
    public void confirm(Object component, Request request, Fufu annotation,
            AnnotatedElement element) throws ConstraintViolatedException {
        throw new ValidationFailedException(new Notes().add(annotation.value(),
                new Note("")));
    }
}
