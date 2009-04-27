package org.seasar.ymir.extension.creator.impl;

import java.util.List;

import org.seasar.ymir.annotation.Meta;

public class Hoe4 {
    private Form4 form4_;

    @Meta(name = "formProperty", value = "form4")
    public List<String> getList() {
        return form4_.getList();
    }
}
