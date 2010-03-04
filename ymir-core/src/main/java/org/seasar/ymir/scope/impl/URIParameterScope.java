package org.seasar.ymir.scope.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.Request;
import org.seasar.ymir.scope.Scope;

/**
 * URIから取り出したパラメータを取得するための便宜的なスコープを表すクラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.7
 */
public class URIParameterScope implements Scope {
    protected S2Container container_;

    @Binding(bindingType = BindingType.MUST)
    public void setS2Container(S2Container container) {
        container_ = container;
    }

    public Object getAttribute(String name, Class<?> type) {
        if (name == null) {
            return null;
        }

        return getRequest().getCurrentDispatch().getURIParameterMap().get(name);
    }

    Request getRequest() {
        return (Request) container_.getComponent(Request.class);
    }

    public void setAttribute(String name, Object value) {
    }

    public Iterator<String> getAttributeNames() {
        return getRequest().getCurrentDispatch().getURIParameterMap().keySet()
                .iterator();
    }
}
