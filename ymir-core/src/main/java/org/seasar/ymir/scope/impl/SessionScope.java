package org.seasar.ymir.scope.impl;

import javax.servlet.http.HttpSession;

/**
 * 現在のHTTPセッションの範囲で有効なオブジェクトを管理するスコープを表すクラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class SessionScope extends AbstractServletScope {
    public Object getAttribute(String name) {
        HttpSession session = getSession(false);
        if (session == null) {
            return null;
        } else {
            return session.getAttribute(name);
        }
    }

    public void setAttribute(String name, Object value) {
        getSession().setAttribute(name, value);
    }
}
