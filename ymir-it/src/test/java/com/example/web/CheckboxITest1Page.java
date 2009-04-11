package com.example.web;

import org.seasar.ymir.scope.annotation.RequestParameter;

public class CheckboxITest1Page {
    private boolean check1 = false;

    private boolean check2 = true;

    public boolean isCheck1() {
        return check1;
    }

    @RequestParameter
    public void setCheck1(boolean check1) {
        this.check1 = check1;
    }

    public boolean isCheck2() {
        return check2;
    }

    @RequestParameter
    public void setCheck2(boolean check2) {
        this.check2 = check2;
    }

    public void _get() {
    }

    public void _post() {
    }
}
