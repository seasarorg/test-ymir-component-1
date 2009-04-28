package org.seasar.ymir.extension.creator.impl;

import java.util.HashMap;
import java.util.Map;

import org.seasar.ymir.extension.creator.BodyDesc;

public class BodyDescImpl implements BodyDesc {
    private String key_;

    private Map<String, Object> root_;

    private String[] dependingClassNames_ = new String[0];

    public BodyDescImpl(String key, Map<String, Object> root,
            String[] dependingClassNames) {
        key_ = key;
        root_ = root;
        dependingClassNames_ = dependingClassNames;
    }

    public BodyDescImpl(String body, String[] dependingClassNames) {
        key_ = KEY_ASIS;
        Map<String, Object> root = new HashMap<String, Object>();
        root.put(PROP_BODY, body);
        root_ = root;
        dependingClassNames_ = dependingClassNames;
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

    public String[] getDependingClassNames() {
        return dependingClassNames_;
    }

    public void setDependingClassNames(String[] classNames) {
        dependingClassNames_ = classNames;
    }
}
