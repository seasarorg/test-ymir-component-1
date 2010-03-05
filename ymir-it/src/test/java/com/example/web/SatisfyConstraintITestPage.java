package com.example.web;

import org.seasar.ymir.constraint.annotation.NotEmpty;
import org.seasar.ymir.constraint.annotation.Satisfy;

public class SatisfyConstraintITestPage {
    @Satisfy(Bean.class)
    public void _get() {
    }

    public static class Bean {
        private String value_;

        public String getValue() {
            return value_;
        }

        @NotEmpty
        public void setValue(String value) {
            value_ = value;
        }
    }
}
