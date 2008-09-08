package org.seasar.ymir.scope.impl;

import java.util.Iterator;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Request;
import org.seasar.ymir.scope.Scope;

/**
 * 拡張リクエストパラメータをインジェクトするための仮想的なスコープを表すクラスです。
 * <p>このスコープはJSONやAMFで渡されるようなパラメータを扱うためのものです。
 * </p>
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.0
 */
public class ExtendedRequestParameterScope implements Scope {
    protected S2Container container_;

    @Binding(bindingType = BindingType.MUST)
    public void setS2Container(S2Container container) {
        container_ = container;
    }

    public Object getAttribute(String name, Class<?> type) {
        if (name == null) {
            return null;
        }

        return getRequest().getExtendedParameter(name);
    }

    Request getRequest() {
        return (Request) container_.getComponent(Request.class);
    }

    public void setAttribute(String name, Object value) {
    }

    public Iterator<String> getAttributeNames() {
        return getRequest().getExtendedParameterNames();
    }

    public String getName() {
        return ExtendedRequestParameterScope.class.getName();
    }
}
