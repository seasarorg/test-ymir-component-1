package com.example.web;

public class ScopeAttributePopulatorTestPage {
    private Aaa aaa_ = new Aaa();

    public Aaa getAaa() {
        return aaa_;
    }

    public void setAaa(Aaa aaa) {
        aaa_ = aaa;
    }

    public void _get() {
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
