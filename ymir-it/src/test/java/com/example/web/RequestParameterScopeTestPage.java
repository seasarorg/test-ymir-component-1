package com.example.web;

import org.seasar.ymir.annotation.In;
import org.seasar.ymir.scope.impl.RequestParameterScope;

public class RequestParameterScopeTestPage {
    private String injectedValue1_;

    private String injectedValue2_;

    private String[] injectedValue3_;

    private String[] injectedValue4_;

    private Integer injectedValue5_;

    private Integer[] injectedValue6_;

    private Dto dto_;

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

    @In(RequestParameterScope.class)
    public Dto getDto() {
        return dto_;
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
