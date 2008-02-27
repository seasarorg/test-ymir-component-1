package org.seasar.ymir.scope.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.util.ContainerUtils;

abstract public class AbstractServletScope implements Scope {
    protected S2Container container_;

    @Binding(bindingType = BindingType.MUST)
    public void setS2Container(S2Container container) {
        container_ = container;
    }

    protected HttpSession getSession() {
        return getSession(true);
    }

    protected HttpServletRequest getRequest() {
        return ContainerUtils.getHttpServletRequest(container_.getRoot());
    }

    protected HttpSession getSession(boolean create) {
        return getRequest().getSession(create);
    }
}
