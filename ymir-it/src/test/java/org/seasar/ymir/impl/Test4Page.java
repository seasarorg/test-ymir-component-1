package org.seasar.ymir.impl;

import org.seasar.ymir.Note;
import org.seasar.ymir.Notes;
import org.seasar.ymir.annotation.Validator;

public class Test4Page {
    private int idx_;

    private String param1_;

    private String param2_ = "";

    public void _post_button(int idx, String param) {
    }

    @Validator("_post_button")
    public Notes validate(int idx, String param1, String param2) {
        idx_ = idx;
        param1_ = param1;
        param2_ = param2;
        return new Notes().add("validate", new Note("validate"));
    }

    public int getIdx() {
        return idx_;
    }

    public String getParam1() {
        return param1_;
    }

    public String getParam2() {
        return param2_;
    }
}
