package org.seasar.ymir.extension.creator.mapping;

import org.seasar.ymir.PathMapping;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.MethodDesc;

public interface ExtraPathMapping {
    Class<? extends PathMapping> getPathMappingClass();

    MethodDesc newActionMethodDesc(DescPool pool, ActionSelectorSeed seed);

    MethodDesc newPrerenderActionMethodDesc(DescPool pool,
            ActionSelectorSeed seed);
}
