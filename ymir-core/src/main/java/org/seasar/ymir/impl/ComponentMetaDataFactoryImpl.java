package org.seasar.ymir.impl;

import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.ComponentMetaData;
import org.seasar.ymir.ComponentMetaDataFactory;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.cache.CacheManager;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.scope.ScopeManager;

public class ComponentMetaDataFactoryImpl implements ComponentMetaDataFactory {
    private ApplicationManager applicationManager_;

    private AnnotationHandler annotationHandler_;

    private ScopeManager scopeManager_;

    private TypeConversionManager typeConversionManager_;

    private Map<Class<?>, ComponentMetaData> metaDataMap_;

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setAnnotationHandler(AnnotationHandler annotationHandler) {
        annotationHandler_ = annotationHandler;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setCacheManager(CacheManager cacheManager) {
        metaDataMap_ = cacheManager.newMap();
    }

    @Binding(bindingType = BindingType.MUST)
    public void setScopeManager(ScopeManager scopeManager) {
        scopeManager_ = scopeManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setTypeConversionManager(
            TypeConversionManager typeConversionManager) {
        typeConversionManager_ = typeConversionManager;
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
        return new ComponentMetaDataImpl(clazz, applicationManager_
                .findContextApplication().getS2Container(), annotationHandler_,
                scopeManager_, typeConversionManager_);
    }
}
