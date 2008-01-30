package org.seasar.ymir.json;

import org.seasar.ymir.annotation.RequestParameter;

public class TestPage {
    private String hoe_;

    private String fuga_;

    public String getHoe() {
        return hoe_;
    }

    @RequestParameter
    public void setHoe(String hoe) {
        hoe_ = hoe;
    }

    public String getFuga() {
        return fuga_;
    }

    public void setFuga(String fuga) {
        fuga_ = fuga;
    }
}
