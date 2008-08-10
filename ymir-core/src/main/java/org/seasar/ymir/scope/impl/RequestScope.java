package org.seasar.ymir.scope.impl;

import java.util.Iterator;

import org.seasar.kvasir.util.collection.EnumerationIterator;

/**
 * 現在のHTTPリクエストの範囲で有効なオブジェクトを管理するスコープを表すクラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class RequestScope extends AbstractServletScope {
    public Object getAttribute(String name) {
        return getRequest().getAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        getRequest().setAttribute(name, value);
    }

    @SuppressWarnings("unchecked")
    public Iterator<String> getAttributeNames() {
        return new EnumerationIterator(getRequest().getAttributeNames());
    }

    public String getName() {
        return SessionScope.class.getName();
    }
}
