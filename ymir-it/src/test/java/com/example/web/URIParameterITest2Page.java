package com.example.web;

import org.seasar.ymir.scope.annotation.RequestParameter;

public class URIParameterITest2Page {
    private String category_;

    private int sequence_;

    public String getCategory() {
        return category_;
    }

    @RequestParameter
    public void setCategory(String category) {
        category_ = category;
    }

    public int getSequence() {
        return sequence_;
    }

    @RequestParameter
    public void setSequence(int sequence) {
        sequence_ = sequence;
    }

    public void _get() {
    }
}
