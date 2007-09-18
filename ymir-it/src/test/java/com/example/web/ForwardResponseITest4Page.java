package com.example.web;

public class ForwardResponseITest4Page {
    private boolean putCalled_;

    private boolean getCalled_;

    public void _put() {
        putCalled_ = true;
    }

    public void _get() {
        getCalled_ = true;
    }

    public boolean isGetCalled() {
        return getCalled_;
    }

    public boolean isPutCalled() {
        return putCalled_;
    }
}
