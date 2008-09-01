package org.seasar.ymir.window.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.window.WindowManager;

/**
 * 同一画面に関してだけ有効なオブジェクトを管理するスコープを表すクラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 *
 * @see WindowManager
 * @author YOKOTA Takehiko
 */
public class WindowScope implements Scope {
    public static final String KEY_SUBSCOPE = WindowManagerImpl.ATTRPREFIX_WINDOW
            + "subScope";

    private WindowManager windowManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setWindowManager(WindowManager windowManager) {
        windowManager_ = windowManager;
    }

    public Object getAttribute(String name, Class<?> type) {
        if (name == null) {
            return null;
        }

        Map<String, Object> subScopeMap = getSubScopeMap(false);
        if (subScopeMap == null) {
            return null;
        }
        return subScopeMap.get(name);
    }

    @SuppressWarnings("unchecked")
    Map<String, Object> getSubScopeMap(boolean create) {
        Map<String, Object> subScopeMap = (Map<String, Object>) windowManager_
                .getScopeAttribute(KEY_SUBSCOPE);
        if (subScopeMap == null && create) {
            subScopeMap = new HashMap<String, Object>();
            windowManager_.setScopeAttribute(KEY_SUBSCOPE, subScopeMap);
        }
        return subScopeMap;
    }

    void removeSubScopeMap() {
        windowManager_.removeScopeAttribute(KEY_SUBSCOPE);
    }

    public void setAttribute(String name, Object value) {
        if (name == null) {
            return;
        }

        if (value == null) {
            Map<String, Object> subScopeMap = getSubScopeMap(false);
            if (subScopeMap == null) {
                return;
            }
            subScopeMap.remove(name);
            if (subScopeMap.isEmpty()) {
                removeSubScopeMap();
            }
        } else {
            getSubScopeMap(true).put(name, value);
        }
    }

    public Iterator<String> getAttributeNames() {
        Map<String, Object> subScopeMap = getSubScopeMap(false);
        if (subScopeMap == null) {
            return new ArrayList<String>().iterator();
        }
        return subScopeMap.keySet().iterator();
    }

    public String getName() {
        return WindowScope.class.getName();
    }
}
