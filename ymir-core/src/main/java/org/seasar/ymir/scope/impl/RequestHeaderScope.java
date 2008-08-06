package org.seasar.ymir.scope.impl;

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
        return getRequest().getHeaders(name);
    }

    public void setAttribute(String name, Object value) {
        throw new UnsupportedOperationException(
                "Can't set request header: name=" + name + ", value=" + value);
    }
}
