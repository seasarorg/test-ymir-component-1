package org.seasar.ymir.impl;

import org.seasar.ymir.Notes;
import org.seasar.ymir.annotation.Validator;
import org.seasar.ymir.constraint.ValidationFailedException;

public class TestPage {
    public void _get() {
    }

    @Validator
    public Notes validate() {
        return new Notes();
    }

    @Validator
    public Notes validate2() {
        return null;
    }

    @Validator
    public void validate3() throws ValidationFailedException {
    }
}
