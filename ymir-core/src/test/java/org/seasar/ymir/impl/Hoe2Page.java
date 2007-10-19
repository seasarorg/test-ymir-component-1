package org.seasar.ymir.impl;

import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.annotation.In;
import org.seasar.ymir.annotation.Out;
import org.seasar.ymir.annotation.Protected;
import org.seasar.ymir.annotation.RequestParameter;
import org.seasar.ymir.scope.impl.SessionScope;

public class Hoe2Page {
    @In(scopeClass = SessionScope.class)
    public void setHoehoe(String hoehoe) {
    }

    @Out(scopeClass = SessionScope.class)
    public String getHoehoe2() {
        return null;
    }

    @Binding
    public void setHoehoe3(String hoehoe3) {
    }

    @Protected
    public void setHoehoe4(String hoehoe4) {
    }

    public void setHoehoe5(FormFile hoehoe5) {
    }

    public void setHoehoe6(FormFile[] hoehoe6) {
    }

    public void setHoehoe7(Map hoehoe7) {
    }

    @In(scopeClass = SessionScope.class)
    public void setFugafuga(String fugafuga) {
    }

    @Out(scopeClass = SessionScope.class)
    public String getFugafuga2() {
        return null;
    }

    @Binding
    public void setFugafuga3(String fugafuga3) {
    }

    @Protected
    public void setFugafuga4(String fugafuga4) {
    }

    public void setFugafuga5(FormFile fugafuga5) {
    }

    public void setFugafuga6(FormFile[] fugafuga6) {
    }

    public void setFugafuga7(Map fugafuga7) {
    }

    public String getFugafuga8() {
        return null;
    }

    @RequestParameter
    public void setFugafuga8(String fugafuga8) {
    }

    public Map getAaa() {
        return null;
    }

    @RequestParameter
    public Map getBbb() {
        return null;
    }

    public void setBbb(Map bbb) {
    }
}
