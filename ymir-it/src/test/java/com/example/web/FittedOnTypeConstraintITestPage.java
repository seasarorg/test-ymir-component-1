package com.example.web;

import java.sql.Date;

import org.seasar.ymir.constraint.annotation.Datetime;
import org.seasar.ymir.constraint.annotation.FittedOnType;

@FittedOnType
public class FittedOnTypeConstraintITestPage {
    private int value_;

    private Date date_;

    private Date date2_;

    public int getValue() {
        return value_;
    }

    public void setValue(int value) {
        value_ = value;
    }

    public Date getDate() {
        return date_;
    }

    public void setDate(Date date) {
        date_ = date;
    }

    public Date getDate2() {
        return date2_;
    }

    @Datetime("yyyyMMdd")
    public void setDate2(Date date2) {
        date2_ = date2;
    }

    public void _get() {
    }
}
