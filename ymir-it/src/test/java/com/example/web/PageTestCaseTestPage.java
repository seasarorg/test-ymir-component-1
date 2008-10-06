package com.example.web;

public class PageTestCaseTestPage {
    private boolean prerenderCallded_;

    public void _get() {
    }

    public void _prerender() {
        prerenderCallded_ = true;
    }

    public boolean isPrerenderCalled() {
        return prerenderCallded_;
    }
}
