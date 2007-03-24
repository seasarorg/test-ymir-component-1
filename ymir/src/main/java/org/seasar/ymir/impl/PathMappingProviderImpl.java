package org.seasar.ymir.impl;

import org.seasar.ymir.PathMapping;
import org.seasar.ymir.PathMappingProvider;

public class PathMappingProviderImpl implements PathMappingProvider {

    private PathMapping[] pathMappings_;

    public PathMapping[] getPathMappings() {

        return pathMappings_;
    }

    public void setPathMappings(PathMapping[] pathMappings) {

        pathMappings_ = pathMappings;
    }
}
