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
 * 同一ウィンドウに関してだけ有効なオブジェクトを管理するスコープを表すクラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 *
 * @see WindowManager
 * @author YOKOTA Takehiko
 */
public class WindowScope implements Scope {
    private WindowManager windowManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setWindowManager(WindowManager windowManager) {
        windowManager_ = windowManager;
    }

    public Object getAttribute(String name, Class<?> type) {
        if (name == null) {
            return null;
        }

        return windowManager_.getScopeAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        if (name == null) {
            return;
        }

        windowManager_.setScopeAttribute(name, value);
    }

    public Iterator<String> getAttributeNames() {
        return windowManager_.getScopeAttributeNames();
    }
}
