package com.example.web;

import org.seasar.ymir.scope.annotation.RequestParameter;

public class RequestParameterITestPage {
    private String value_;

    private Dto dto_ = new Dto();

    public String getValue() {
        return value_;
    }

    @RequestParameter
    public void setValue(String value) {
        value_ = value;
    }

    @RequestParameter
    public Dto getDto() {
        return dto_;
    }

    public void _get() {
    }

    public static class Dto {
        private String aaa_;

        public String getAaa() {
            return aaa_;
        }

        public void setAaa(String aaa) {
            aaa_ = aaa;
        }
    }
}
