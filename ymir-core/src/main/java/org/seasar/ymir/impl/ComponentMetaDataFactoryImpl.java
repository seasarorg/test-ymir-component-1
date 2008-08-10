package org.seasar.ymir.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.seasar.cms.pluggable.hotdeploy.AbstractHotdeployEventListener;
import org.seasar.cms.pluggable.util.HotdeployEventUtils;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.ComponentMetaData;
import org.seasar.ymir.ComponentMetaDataFactory;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.hotdeploy.HotdeployManager;

public class ComponentMetaDataFactoryImpl implements ComponentMetaDataFactory {
    private ApplicationManager applicationManager_;

    private AnnotationHandler annotationHandler_;

    private HotdeployManager hotdeployManager_;

    private TypeConversionManager typeConversionManager_;

    private Map<Class<?>, ComponentMetaData> metaDataMap_ = new ConcurrentHashMap<Class<?>, ComponentMetaData>();

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setAnnotationHandler(AnnotationHandler annotationHandler) {
        annotationHandler_ = annotationHandler;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setHotdeployManager(HotdeployManager hotdeployManager) {
        hotdeployManager_ = hotdeployManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setTypeConversionManager(
            TypeConversionManager typeConversionManager) {
        typeConversionManager_ = typeConversionManager;
    }

    public ComponentMetaDataFactoryImpl() {
        HotdeployEventUtils.add(new AbstractHotdeployEventListener() {
            public void stop() {
                metaDataMap_.clear();
            }
        });
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
                hotdeployManager_, typeConversionManager_);
    }
}
