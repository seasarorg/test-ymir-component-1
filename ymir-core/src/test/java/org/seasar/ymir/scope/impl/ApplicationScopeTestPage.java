package org.seasar.ymir.scope.impl;

import org.seasar.ymir.annotation.In;
import org.seasar.ymir.annotation.Out;

public class ApplicationScopeTestPage {
    private String injectedValue_;

    @In(ApplicationScope.class)
    public void setInjectedValue(String value) {
        injectedValue_ = value;
    }

    public String getInjectedValue() {
        return injectedValue_;
    }

    @Out(ApplicationScope.class)
    public String getOutjectedValue() {
        return "OUTJECTED_VALUE";
    }
}
