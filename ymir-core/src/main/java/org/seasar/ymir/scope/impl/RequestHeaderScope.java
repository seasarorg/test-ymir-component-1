package org.seasar.ymir.scope.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * リクエストヘッダをインジェクトするための仮想的なスコープを表すクラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class RequestHeaderScope extends AbstractServletScope {
    public Object getAttribute(String name) {
        List<String> list = new ArrayList<String>();
        for (Enumeration<?> enm = getRequest().getHeaders(name); enm
                .hasMoreElements();) {
            list.add((String) enm.nextElement());
        }
        return list.toArray(new String[0]);
    }

    public void setAttribute(String name, Object value) {
        throw new UnsupportedOperationException(
                "Can't set request header: name=" + name + ", value=" + value);
    }
}
