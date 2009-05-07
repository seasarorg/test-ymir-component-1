package org.seasar.ymir.hotdeploy.fitter.impl;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.hotdeploy.fitter.HotdeployFitter;

abstract public class AbstractFitter<T> implements HotdeployFitter<T> {
    private HotdeployManager hotdeployManager_;

    @Binding(bindingType = BindingType.MUST)
    final public void setHotdeployManager(HotdeployManager hotdeployManager) {
        hotdeployManager_ = hotdeployManager;
    }

    final public HotdeployManager getHotdeployManager() {
        return hotdeployManager_;
    }
}
