package org.seasar.ymir.scope.impl;

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
}
