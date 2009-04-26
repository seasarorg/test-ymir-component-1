package com.example.web;

import org.seasar.ymir.FormFile;
import org.seasar.ymir.scope.annotation.RequestParameter;

public class ScopeInterceptorITest9Page {
    private String[] string_;

    private FormFile[] file_;

    private Object[] object_;

    @RequestParameter
    public void setString(String[] string) {
        string_ = string;
    }

    @RequestParameter
    public void setFile(FormFile[] file) {
        file_ = file;
    }

    @RequestParameter
    public void setObject(Object[] object) {
        object_ = object;
    }

    public void _get() {
    }

    public String[] getString() {
        return string_;
    }

    public FormFile[] getFile() {
        return file_;
    }

    public Object[] getObject() {
        return object_;
    }

}
