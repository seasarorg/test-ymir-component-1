package org.seasar.ymir.impl;

import org.seasar.ymir.Note;
import org.seasar.ymir.Notes;
import org.seasar.ymir.annotation.Validator;

public class Test3Page {
    public void _get() {
    }

    public void _post() {
    }

    @Validator("_post")
    public Notes validate() {
        return new Notes().add("validate", new Note("validate"));
    }
}
