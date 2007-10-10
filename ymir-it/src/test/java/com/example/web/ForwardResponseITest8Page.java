package com.example.web;

public class ForwardResponseITest8Page {
    private boolean renderCalled_;

    public String _get() {
        return "/forwardResponseITest8.forward.html";
    }

    public void _render() {
        renderCalled_ = true;
    }

    public boolean isRenderCalled() {
        return renderCalled_;
    }
}
