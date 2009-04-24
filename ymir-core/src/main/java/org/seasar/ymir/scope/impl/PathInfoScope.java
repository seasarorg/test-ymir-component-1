package org.seasar.ymir.scope.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.Request;
import org.seasar.ymir.scope.Scope;

/**
 * 現在のリクエストパスに付与されているpathInfoを取得するための便宜的なスコープを表すクラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
public class PathInfoScope implements Scope {
    private static final List<String> ATTRIBUTE_NAME_LIST = Collections
            .unmodifiableList(Arrays.asList("pathInfo"));

    protected S2Container container_;

    @Binding(bindingType = BindingType.MUST)
    public void setS2Container(S2Container container) {
        container_ = container;
    }

    public Object getAttribute(String name, Class<?> type) {
        if (name == null) {
            return null;
        }

        return getRequest().getCurrentDispatch().getPathInfo();
    }

    Request getRequest() {
        return (Request) container_.getComponent(Request.class);
    }

    public void setAttribute(String name, Object value) {
    }

    public Iterator<String> getAttributeNames() {
        return ATTRIBUTE_NAME_LIST.iterator();
    }
}
