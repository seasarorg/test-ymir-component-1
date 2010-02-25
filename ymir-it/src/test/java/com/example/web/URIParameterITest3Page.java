package com.example.web;

import org.seasar.ymir.scope.annotation.URIParameter;

public class URIParameterITest3Page {
    private String category_;

    private int sequence_;

    public String getCategory() {
        return category_;
    }

    @URIParameter
    public void setCategory(String category) {
        category_ = category;
    }

    public int getSequence() {
        return sequence_;
    }

    @URIParameter
    public void setSequence(int sequence) {
        sequence_ = sequence;
    }

    public void _get() {
    }
}
