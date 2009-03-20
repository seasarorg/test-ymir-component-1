package org.seasar.ymir.impl;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.PathMapping;
import org.seasar.ymir.PathMappingProvider;

public class PathMappingProviderImpl implements PathMappingProvider {
    private PathMapping[] pathMappings_;

    private ActionManager actionManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setActionManager(ActionManager actionManager) {
        actionManager_ = actionManager;
    }

    @Binding("container.findComponents(@org.seasar.ymir.PathMapping@class)")
    public void setPathMappings(PathMapping[] pathMappings) {
        pathMappings_ = pathMappings;
    }

    public PathMapping[] getPathMappings() {
        return pathMappings_;
    }

    @InitMethod
    public void initialize() {
        for (PathMapping pathMapping : pathMappings_) {
            pathMapping.setActionManager(actionManager_);
        }
    }
}
