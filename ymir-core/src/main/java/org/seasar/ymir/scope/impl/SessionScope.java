package org.seasar.ymir.scope.impl;

import java.util.Iterator;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.session.SessionManager;

/**
 * 現在のHTTPセッションの範囲で有効なオブジェクトを管理するスコープを表すクラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class SessionScope implements Scope {
    private SessionManager sessionManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setSessionManager(SessionManager sessionManager) {
        sessionManager_ = sessionManager;
    }

    public Object getAttribute(String name) {
        return sessionManager_.getAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        sessionManager_.setAttribute(name, value);
    }

    @SuppressWarnings("unchecked")
    public Iterator<String> getAttributeNames() {
        return sessionManager_.getAttributeNames();
    }

    public String getName() {
        return SessionScope.class.getName();
    }
}
