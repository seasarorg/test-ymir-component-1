package org.seasar.ymir.impl;

import org.seasar.ymir.annotation.SuppressConstraints;
import org.seasar.ymir.constraint.ConstraintType;

@Fuga("saru")
@Fufu("tora")
public class Hoe {

    private String fuga_;

    @Fuga("fugar")
    public String getFuga() {
        return fuga_;
    }

    @Fuga("fuga")
    public void setFuga(String fuga) {
        fuga_ = fuga;
    }

    @Fuga("post")
    public void _post() {
    }

    @Fuga("render")
    public void _render() {
    }

    @SuppressConstraints
    @Fuga("get")
    public void _get() {
    }

    @SuppressConstraints(ConstraintType.VALIDATION)
    @Fuga("head")
    public void _head() {
    }
}
