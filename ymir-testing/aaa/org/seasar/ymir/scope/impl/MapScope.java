package org.seasar.ymir.scope.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.seasar.ymir.scope.Scope;

public class MapScope implements Scope {
    private Map<String, Object> map_ = new HashMap<String, Object>();

    public Object getAttribute(String name, Class<?> type) {
        if (name == null) {
            return null;
        }

        return map_.get(name);
    }

    public Iterator<String> getAttributeNames() {
        return map_.keySet().iterator();
    }

    public void setAttribute(String name, Object value) {
        if (name == null) {
            return;
        }

        map_.put(name, value);
    }

    public void clear() {
        map_.clear();
    }
}
