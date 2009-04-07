package com.example.web;

import org.seasar.ymir.token.constraint.annotation.TokenRequired;

public class TokenITestPage {
    @TokenRequired
    public void _get() {
    }

    @TokenRequired(reset = false)
    public void _get_notReset() {
    }

    @TokenRequired(throwException = true)
    public void _get_throwException() {
    }
}
