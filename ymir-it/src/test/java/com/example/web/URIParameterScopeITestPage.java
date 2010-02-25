package com.example.web;

import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.impl.URIParameterScope;

public class URIParameterScopeITestPage {
    private String category_;

    private String title_;

    public String getCategory() {
        return category_;
    }

    public String getTitle() {
        return title_;
    }

    @In(URIParameterScope.class)
    public void setCategory(String category) {
        category_ = category;
    }

    @In(URIParameterScope.class)
    public void setTitle(String title) {
        title_ = title;
    }

    public void _get() {
    }
}
