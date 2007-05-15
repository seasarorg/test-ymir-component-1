package org.seasar.ymir.extension.creator.impl;

import java.util.HashMap;
import java.util.Map;

import org.seasar.ymir.extension.creator.BodyDesc;

public class BodyDescImpl implements BodyDesc {

    private static final String KEY_ASIS = "asIs";

    private static final String PROP_BODY = "body";

    private String key_;

    private Map<String, Object> root_;

    public BodyDescImpl(String key, Map<String, Object> root) {

        key_ = key;
        root_ = root;
    }

    public BodyDescImpl(String body) {

        key_ = KEY_ASIS;
        Map<String, Object> root = new HashMap<String, Object>();
        root.put(PROP_BODY, body);
        root_ = root;
    }

    public Object clone() {

        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getKey() {
        return key_;
    }

    public void setKey(String key) {
        key_ = key;
    }

    public Map<String, Object> getRoot() {
        return root_;
    }

    public void setRoot(Map<String, Object> root) {
        root_ = root;
    }
}
