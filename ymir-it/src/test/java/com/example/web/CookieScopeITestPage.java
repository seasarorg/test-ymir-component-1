package com.example.web;

import javax.servlet.http.Cookie;

import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Out;
import org.seasar.ymir.scope.impl.CookieScope;

public class CookieScopeITestPage {
    private Cookie hoge_;

    private String value_;

    @In(CookieScope.class)
    public void setHoge(Cookie hoge) {
        hoge_ = hoge;
    }

    @In(CookieScope.class)
    public void setValue(String value) {
        value_ = value;
    }

    public void _post_login() {
        hoge_ = new Cookie("hoge", "fuga");
        value_ = "VALUE";
    }

    @Out(CookieScope.class)
    public Cookie getHoge() {
        return hoge_;
    }

    @Out(CookieScope.class)
    public String getValue() {
        return value_;
    }
}
