package com.example.web;

import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.scope.handler.impl.ScopeAttributePopulatorImpl;
import org.seasar.ymir.scope.impl.RequestParameterScope;
import org.seasar.ymir.testing.YmirTestCase;

public class ScopeAttributePopulatorTestPage {
    private Aaa aaa_ = new Aaa();

    private YmirTestCase testCase_;

    public void setTestCase(YmirTestCase testCase) {
        testCase_ = testCase;
    }

    public Aaa getAaa() {
        return aaa_;
    }

    public void setAaa(Aaa aaa) {
        aaa_ = aaa;
    }

    public void _get() {
        ScopeAttributePopulatorImpl target = new ScopeAttributePopulatorImpl(
                testCase_.getComponent(RequestParameterScope.class), testCase_
                        .getComponent(AnnotationHandler.class), testCase_
                        .getComponent(ScopeManager.class), testCase_
                        .getComponent(TypeConversionManager.class));
        try {
            target.addEntry(getClass().getMethod("getAaa", new Class[0]),
                    false, new String[0]);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        target.populateTo(this, null);
    }

    public static class Aaa {
        private String bbb_;

        public String getBbb() {
            return bbb_;
        }

        public void setBbb(String bbb) {
            bbb_ = bbb;
        }
    }
}
