package org.seasar.ymir;

import java.util.Map;

import org.seasar.ymir.PageMetaData;

public interface PageProcessor {
    void injectRequestParameters(Object page, PageMetaData metaData,
            Map<String, String[]> properties);

    void injectRequestFileParameters(Object page,
            PageMetaData metaData, Map<String, FormFile[]> properties);

    void injectContextAttributes(Object page, PageMetaData metaData,
            String actionName);

    void outjectContextAttributes(Object page, PageMetaData metaData,
            String actionName);

    void invokeMethods(Object page, PageMetaData metaData, Phase phase);
}
