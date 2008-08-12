package com.example.web;

import java.util.Date;

import org.seasar.ymir.annotation.In;
import org.seasar.ymir.scope.impl.SessionScope;

public class Scope2Page {
    private String string_;

    private Date date_;

    @In(SessionScope.class)
    public void setString(String value) {
        string_ = value;
    }

    @In(SessionScope.class)
    public void setDate(Date date) {
        date_ = date;
    }

    public void _get() {
    }
}
