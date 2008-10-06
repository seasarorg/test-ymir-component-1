package com.example.web;

public class ForwardResponseITest5Page {
    private boolean prerenderCalled_;

    public String _get() {
        return "/forwardResponseITest6.html";
    }

    public String _post() {
        return "/forwardResponseITest7.html";
    }

    public void _prerender() {
        prerenderCalled_ = true;
    }

    public boolean isPrerenderCalled() {
        return prerenderCalled_;
    }
}
