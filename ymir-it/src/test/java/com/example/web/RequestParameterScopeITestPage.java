package com.example.web;

import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.RequestParameter;
import org.seasar.ymir.scope.impl.RequestParameterScope;

public class RequestParameterScopeITestPage {
    private String injectedValue1_;

    private String injectedValue2_;

    private String[] injectedValue3_;

    private String[] injectedValue4_;

    private Integer injectedValue5_;

    private Integer[] injectedValue6_;

    private String injectedValue7_;

    @In(RequestParameterScope.class)
    public void setInjectedValue1(String value) {
        injectedValue1_ = value;
    }

    @In(RequestParameterScope.class)
    public void setInjectedValue2(String value) {
        injectedValue2_ = value;
    }

    @In(RequestParameterScope.class)
    public void setInjectedValue3(String[] value) {
        injectedValue3_ = value;
    }

    @In(RequestParameterScope.class)
    public void setInjectedValue4(String[] value) {
        injectedValue4_ = value;
    }

    @In(RequestParameterScope.class)
    public void setInjectedValue5(Integer injectedValue5) {
        injectedValue5_ = injectedValue5;
    }

    @In(RequestParameterScope.class)
    public void setInjectedValue6(Integer[] injectedValue6) {
        injectedValue6_ = injectedValue6;
    }

    @RequestParameter("i7")
    public void setInjectedValue7(String injectedValue7) {
        injectedValue7_ = injectedValue7;
    }

    public String getInjectedValue1() {
        return injectedValue1_;
    }

    public String getInjectedValue2() {
        return injectedValue2_;
    }

    public String[] getInjectedValue3() {
        return injectedValue3_;
    }

    public String[] getInjectedValue4() {
        return injectedValue4_;
    }

    public Integer getInjectedValue5() {
        return injectedValue5_;
    }

    public Integer[] getInjectedValue6() {
        return injectedValue6_;
    }

    public String getInjectedValue7() {
        return injectedValue7_;
    }

    public void _get() {
    }
}
