package org.seasar.ymir.impl;

import org.seasar.ymir.Note;
import org.seasar.ymir.Notes;
import org.seasar.ymir.annotation.Validator;
import org.seasar.ymir.constraint.ValidationFailedException;

public class Test2Page {
    public void _get() {
    }

    @Validator
    public Notes validate() {
        return new Notes().add("validate", new Note("validate"));
    }

    @Validator
    public void validate2() throws ValidationFailedException {
        throw new ValidationFailedException(new Notes().add("validate2",
                new Note("validate2")));
    }
}
