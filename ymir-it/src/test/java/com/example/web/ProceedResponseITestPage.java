package com.example.web;

public class ProceedResponseITestPage {
    private boolean renderCalled_;

    public String _get() {
        return "proceed:/proceedResponseITest2.html";
    }

    public String _post() {
        return "proceed:/proceedResponseITest3.html";
    }

    public void _render() {
        renderCalled_ = true;
    }

    public boolean isRenderCalled() {
        return renderCalled_;
    }
}
