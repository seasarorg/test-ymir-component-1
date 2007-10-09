package com.example.web;

public class ForwardResponseITest5Page {
    private boolean renderCalled_;

    public String _get() {
        return "/forwardResponseITest6.html";
    }

    public String _post() {
        return "/forwardResponseITest7.html";
    }

    public void _render() {
        renderCalled_ = true;
    }

    public boolean isRenderCalled() {
        return renderCalled_;
    }
}
