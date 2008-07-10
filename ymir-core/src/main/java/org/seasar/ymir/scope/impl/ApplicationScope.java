package org.seasar.ymir.scope.impl;

import javax.servlet.ServletContext;

import org.seasar.ymir.util.ContainerUtils;

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

    ServletContext getServletContext() {
        return ContainerUtils.getServletContext(container_.getRoot());
    }
}
