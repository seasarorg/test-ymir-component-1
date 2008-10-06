package com.example.web;

public class ForwardResponseITest8Page {
    private boolean prerenderCalled_;

    public String _get() {
        return "/forwardResponseITest8.forward.html";
    }

    public void _prerender() {
        prerenderCalled_ = true;
    }

    public boolean isPrerenderCalled() {
        return prerenderCalled_;
    }
}
