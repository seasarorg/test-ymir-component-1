package org.seasar.ymir;

import java.util.Map;

import org.seasar.ymir.PagePropertyMetaData;

public interface PageProcessor {
    void injectRequestParameters(Object page, PagePropertyMetaData metaData,
            Map<String, String[]> properties);

    void injectRequestFileParameters(Object page,
            PagePropertyMetaData metaData, Map<String, FormFile[]> properties);

    void injectContextAttributes(Object page, String actionName,
            PagePropertyMetaData metaData);

    void outjectContextAttributes(Object page, String actionName,
            PagePropertyMetaData metaData);
}
