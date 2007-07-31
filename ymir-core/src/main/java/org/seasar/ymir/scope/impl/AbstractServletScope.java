package org.seasar.ymir.scope.impl;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.S2Container;
import org.seasar.ymir.scope.Scope;

abstract public class AbstractServletScope implements Scope {
    protected S2Container container_;

    public void setS2Container(S2Container container) {
        container_ = container;
    }

    protected ExternalContext getExternalContext() {
        return container_.getRoot().getExternalContext();
    }
}
