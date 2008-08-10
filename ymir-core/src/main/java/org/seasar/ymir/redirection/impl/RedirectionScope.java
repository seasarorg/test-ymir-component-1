package org.seasar.ymir.redirection.impl;

import java.util.Iterator;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.redirection.RedirectionManager;
import org.seasar.ymir.scope.Scope;

/**
 * リダイレクト先でだけ有効なオブジェクトを管理するスコープを表すクラスです。
 * <p>リダイレクト先に一時的に何らかの情報を受け渡したい場合に利用されます。
 * </p>
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class RedirectionScope implements Scope {
    private RedirectionManager redirectionManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setRedirectionManager(RedirectionManager redirectionManager) {
        redirectionManager_ = redirectionManager;
    }

    public Object getAttribute(String name) {
        return redirectionManager_.getScopeAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        if (value == null) {
            redirectionManager_.removeScopeAttribute(name);
        } else {
            redirectionManager_.setScopeAttribute(name, value);
        }
    }

    public Iterator<String> getAttributeNames() {
        return redirectionManager_.getScopeAttributeNames();
    }

    public String getName() {
        return RedirectionScope.class.getName();
    }
}
