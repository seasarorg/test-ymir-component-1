package com.example.web;

import org.seasar.ymir.annotation.RequestParameter;

public class ForwardResponseITest2Page {
    private String param1_;

    private String param2_;

    public String getParam1() {
        return param1_;
    }

    @RequestParameter
    public void setParam1(String param1) {
        param1_ = param1;
    }

    public String getParam2() {
        return param2_;
    }

    @RequestParameter
    public void setParam2(String param2) {
        param2_ = param2;
    }
}
