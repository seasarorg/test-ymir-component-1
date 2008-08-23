package com.example.web;

import org.seasar.ymir.aop.annotation.HTTPS;

public class HTTPSPage {
    @HTTPS
    public String _get() {
        return "redirect:/path";
    }
}
