package org.seasar.ymir.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bean {
    private Aaa[] aaas_ = new Aaa[] { new Aaa(), new Aaa(), new Aaa() };

    public Aaa getAaa(int index) {
        return aaas_[index];
    }

    public Aaa[] getAaas() {
        return aaas_;
    }

    public List<Aaa> getAaaList() {
        return null;
    }

    public static class Aaa {
        private Map<String, Bbb> bbbMap_ = new HashMap<String, Bbb>();

        public Aaa() {
            bbbMap_.put("key", new Bbb());
        }

        public Bbb getBbb(String key) {
            return bbbMap_.get(key);
        }
    }

    public static class Bbb {
        private String simple_;

        private String[] indexed_ = new String[3];

        private Map<String, String> mapped_ = new HashMap<String, String>();

        public String getSimple() {
            return simple_;
        }

        public void setSimple(String simple) {
            simple_ = simple;
        }

        public String getIndexed(int index) {
            return indexed_[index];
        }

        public void setIndexed(int index, String indexed) {
            indexed_[index] = indexed;
        }

        public String getMapped(String key) {
            return mapped_.get(key);
        }

        public void setMapped(String key, String mapped) {
            mapped_.put(key, mapped);
        }
    }
}
