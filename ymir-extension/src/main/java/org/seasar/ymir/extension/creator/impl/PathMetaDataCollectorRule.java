package org.seasar.ymir.extension.creator.impl;

import java.util.List;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.WebappSourceResourceCollector.Rule;

public class PathMetaDataCollectorRule implements Rule<PathMetaData> {
    private SourceCreator sourceCreator_;

    public PathMetaDataCollectorRule(SourceCreator sourceCreator) {
        sourceCreator_ = sourceCreator;
    }

    public void add(String path, List<PathMetaData> resourceList) {
        addPathMetaDataIfNecessary(path, HttpMethod.GET, resourceList);
        addPathMetaDataIfNecessary(path, HttpMethod.POST, resourceList);
    }

    void addPathMetaDataIfNecessary(String path, HttpMethod method,
            List<PathMetaData> pathList) {
        PathMetaData pathMetaData = new LazyPathMetaData(sourceCreator_, path,
                method, path);
        if (pathMetaData.getComponentName() != null && !pathMetaData.isDenied()) {
            pathList.add(pathMetaData);
        }
    }
}
