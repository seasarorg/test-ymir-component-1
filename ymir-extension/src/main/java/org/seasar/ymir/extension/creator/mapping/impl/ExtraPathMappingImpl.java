package org.seasar.ymir.extension.creator.mapping.impl;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.PathMapping;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.mapping.ActionSelectorSeed;
import org.seasar.ymir.extension.creator.mapping.ExtraPathMapping;
import org.seasar.ymir.extension.creator.mapping.PathMappingExtraData;

public class ExtraPathMappingImpl implements ExtraPathMapping {
    private PathMappingExtraData<PathMapping> extraData_;

    private MatchedPathMapping matched_;

    private String path_;

    private HttpMethod method_;

    @SuppressWarnings("unchecked")
    public ExtraPathMappingImpl(PathMappingExtraData<?> extraData,
            MatchedPathMapping matched, String path, HttpMethod method) {
        extraData_ = (PathMappingExtraData<PathMapping>) extraData;
        matched_ = matched;
        path_ = path;
        method_ = method;
    }

    public Class<? extends PathMapping> getPathMappingClass() {
        return extraData_.getPathMappingClass();
    }

    public MethodDesc newActionMethodDesc(ActionSelectorSeed seed) {
        return extraData_.newActionMethodDesc(matched_.getPathMapping(),
                matched_.getVariableResolver(), path_, method_, seed);
    }

    public MethodDesc newPrerenderActionMethodDesc(ActionSelectorSeed seed) {
        return extraData_.newRenderActionMethodDesc(matched_.getPathMapping(),
                matched_.getVariableResolver(), path_, method_, seed);
    }
}
