package com.example.web;

import org.seasar.ymir.session.annotation.InvalidateSession;

public class InvalidateSessionITestPage {
    @InvalidateSession
    public void _get() {
    }

    @InvalidateSession
    public void _get_exception() {
        throw new RuntimeException();
    }
}
