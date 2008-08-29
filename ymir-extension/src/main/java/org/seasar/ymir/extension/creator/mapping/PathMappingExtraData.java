package org.seasar.ymir.extension.creator.mapping;

import org.seasar.kvasir.util.el.VariableResolver;
import org.seasar.ymir.PathMapping;
import org.seasar.ymir.extension.creator.MethodDesc;

public interface PathMappingExtraData<P extends PathMapping> {
    Class<P> getPathMappingClass();

    MethodDesc newActionMethodDesc(P pathMapping, VariableResolver resolver,
            String path, String method, ActionSelectorSeed seed);

    MethodDesc newRenderActionMethodDesc(P pathMapping,
            VariableResolver resolver, String path, String method,
            ActionSelectorSeed seed);
}
