package org.seasar.ymir.scope.impl;

import java.util.Iterator;

import org.seasar.kvasir.util.collection.EnumerationIterator;

/**
 * Webアプリケーション全体で有効なオブジェクトを管理するスコープを表すクラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class ApplicationScope extends AbstractServletScope {
    public Object getAttribute(String name) {
        return getServletContext().getAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        getServletContext().setAttribute(name, value);
    }

    @SuppressWarnings("unchecked")
    public Iterator<String> getAttributeNames() {
        return new EnumerationIterator(getServletContext().getAttributeNames());
    }

    public String getName() {
        return ApplicationScope.class.getName();
    }
}
