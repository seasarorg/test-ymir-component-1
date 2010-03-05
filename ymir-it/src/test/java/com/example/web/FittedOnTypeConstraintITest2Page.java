package com.example.web;

import org.seasar.ymir.constraint.annotation.FittedOnType;

public class FittedOnTypeConstraintITest2Page {
    private int value_;

    public int getValue() {
        return value_;
    }

    @FittedOnType
    public void setValue(int value) {
        value_ = value;
    }

    public void _get() {
    }
}
