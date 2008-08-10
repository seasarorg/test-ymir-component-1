package org.seasar.ymir.scope.impl;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.seasar.kvasir.util.collection.EnumerationIterator;

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

    @SuppressWarnings("unchecked")
    public Iterator<String> getAttributeNames() {
        HttpSession session = getSession(false);
        if (session == null) {
            return new ArrayList<String>().iterator();
        } else {
            return new EnumerationIterator(session.getAttributeNames());
        }
    }

    public String getName() {
        return SessionScope.class.getName();
    }
}
