package com.example.web;

import org.seasar.ymir.aop.annotation.HTTP;

public class HTTPPage {
    @HTTP
    public String _get() {
        return "redirect:/path";
    }
}
