package org.seasar.ymir.impl;

import org.seasar.ymir.Note;
import org.seasar.ymir.Notes;
import org.seasar.ymir.annotation.Validator;

public class Test4Page {
    private int idx_;

    public void _post_button(int idx) {
    }

    @Validator("_post_button")
    public Notes validate(int idx) {
        idx_ = idx;
        return new Notes().add("validate", new Note("validate"));
    }

    public int getIdx() {
        return idx_;
    }
}
