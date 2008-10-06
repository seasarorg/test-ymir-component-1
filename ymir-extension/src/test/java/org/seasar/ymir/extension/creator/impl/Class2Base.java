package org.seasar.ymir.extension.creator.impl;

import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Out;
import org.seasar.ymir.scope.impl.SessionScope;

public class Class2Base {
    private String value_;

    @Out(SessionScope.class)
    public String getValue() {
        return value_;
    }

    @In(SessionScope.class)
    public void setValue(String value) {
        value_ = value;
    }
}
