package com.example.web;

public class VoidResponseITestPage {
    private boolean rendered_;

    public String _get() {
        return null;
    }

    public void _render() {
        rendered_ = true;
    }

    public boolean isRendered() {
        return rendered_;
    }
}
