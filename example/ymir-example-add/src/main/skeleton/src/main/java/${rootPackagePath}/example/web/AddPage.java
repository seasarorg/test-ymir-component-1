package com.example.web;

import org.seasar.ymir.constraint.annotation.Required;

public class AddPage extends AddPageBase {
    @Override
    @Required( { "left", "right" })
    public void _post() {
        result_ = left_ + right_;
    }
}
