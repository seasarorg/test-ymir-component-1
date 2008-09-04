package com.example.web;

public class PageTestCaseTestPage {
    private boolean renderCallded_;

    public void _get() {
    }

    public void _render() {
        renderCallded_ = true;
    }

    public boolean isRenderCalled() {
        return renderCallded_;
    }
}
