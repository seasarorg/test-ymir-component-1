package com.example.web;

import java.util.Date;

import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.impl.SessionScope;

public class Scope2Page {
    @In(SessionScope.class)
    public void setString(String value) {
    }

    @In(SessionScope.class)
    public void setDate(Date date) {
    }

    public void _get() {
    }
}
