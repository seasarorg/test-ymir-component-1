package org.seasar.ymir.scope.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.seasar.kvasir.util.collection.EnumerationIterator;

/**
 * リクエストヘッダをインジェクトするための仮想的なスコープを表すクラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class RequestHeaderScope extends AbstractServletScope {
    public Object getAttribute(String name, Class<?> type) {
        if (name == null) {
            return null;
        }

        List<String> list = new ArrayList<String>();
        for (Enumeration<?> enm = getRequest().getHeaders(name); enm
                .hasMoreElements();) {
            list.add((String) enm.nextElement());
        }
        return list.toArray(new String[0]);
    }

    public void setAttribute(String name, Object value) {
    }

    @SuppressWarnings("unchecked")
    public Iterator<String> getAttributeNames() {
        return new EnumerationIterator(getRequest().getHeaderNames());
    }

    public String getName() {
        return RequestHeaderScope.class.getName();
    }
}
