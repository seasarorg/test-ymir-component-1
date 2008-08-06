package org.seasar.ymir.scope.impl;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.Request;
import org.seasar.ymir.scope.Scope;

/**
 * {@link FormFile}型のリクエストパラメータをインジェクトするための仮想的なスコープを表すクラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @see RequestParameterScope
 * @author YOKOTA Takehiko
 */
public class FileRequestParameterScope implements Scope {
    protected S2Container container_;

    @Binding(bindingType = BindingType.MUST)
    public void setS2Container(S2Container container) {
        container_ = container;
    }

    public Object getAttribute(String name) {
        return getRequest().getFileParameterValues(name);
    }

    Request getRequest() {
        return (Request) container_.getComponent(Request.class);
    }

    public void setAttribute(String name, Object value) {
        throw new UnsupportedOperationException("Can't set parameter: name="
                + name + ", value=" + value);
    }
}
