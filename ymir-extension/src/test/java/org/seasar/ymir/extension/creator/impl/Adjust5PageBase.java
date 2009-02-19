package org.seasar.ymir.extension.creator.impl;

import java.util.List;

import org.seasar.ymir.annotation.Meta;

public class Adjust5PageBase {
    @Meta(name = "initialValue", value = "new org.seasar.ymir.util.FlexibleList<Integer>()")
    protected List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
