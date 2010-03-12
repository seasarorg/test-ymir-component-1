package org.seasar.ymir.impl;

import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.ComponentMetaData;
import org.seasar.ymir.ComponentMetaDataFactory;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.cache.CacheManager;

public class ComponentMetaDataFactoryImpl implements ComponentMetaDataFactory {
    private ActionManager actionManager_;

    private AnnotationHandler annotationHandler_;

    private Map<Class<?>, ComponentMetaData> metaDataMap_;

    @Binding(bindingType = BindingType.MUST)
    public void setAnnotationHandler(AnnotationHandler annotationHandler) {
        annotationHandler_ = annotationHandler;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setActionManager(ActionManager actionManager) {
        actionManager_ = actionManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setCacheManager(CacheManager cacheManager) {
        metaDataMap_ = cacheManager.newMap();
    }

    public ComponentMetaData getInstance(Class<?> clazz) {
        ComponentMetaData metaData = metaDataMap_.get(clazz);
        if (metaData == null) {
            metaData = newInstance(clazz);
            metaDataMap_.put(clazz, metaData);
        }
        return metaData;
    }

    protected ComponentMetaData newInstance(Class<?> clazz) {
        return new ComponentMetaDataImpl(clazz, actionManager_,
                annotationHandler_);
    }
}
