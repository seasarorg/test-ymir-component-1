package com.example.web;

import java.util.Date;

import org.seasar.ymir.constraint.annotation.Datetime;

public class ParamSelfITest2Page {
    private Date date_;

    public Date getDate() {
        return date_;
    }

    @Datetime("yyyyMMdd")
    public void setDate(Date date) {
        date_ = date;
    }

    public void _get() {
    }
}
