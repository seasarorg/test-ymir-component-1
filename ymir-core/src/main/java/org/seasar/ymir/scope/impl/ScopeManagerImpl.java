package org.seasar.ymir.scope.impl;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.cache.CacheManager;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.scope.AttributeNotFoundRuntimeException;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.scope.ScopeMetaData;
import org.seasar.ymir.util.ClassUtils;

public class ScopeManagerImpl implements ScopeManager {
    private ApplicationManager applicationManager_;

    private AnnotationHandler annotationHandler_;

    private HotdeployManager hotdeployManager_;

    private TypeConversionManager typeConversionManager_;

    private Map<Class<?>, ScopeMetaData> metaDataMap_;

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
    public void setHotdeployManager(HotdeployManager hotdeployManager) {
        hotdeployManager_ = hotdeployManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setTypeConversionManager(
            TypeConversionManager typeConversionManager) {
        typeConversionManager_ = typeConversionManager;
    }

    public <T> T getAttribute(Scope scope, String name, Class<T> type,
            boolean required,
            boolean convertNullToDefaultValueWhereTypeIsPrimitive)
            throws AttributeNotFoundRuntimeException {
        return getAttribute(scope, name, type, null, required,
                convertNullToDefaultValueWhereTypeIsPrimitive);
    }

    public <T> T getAttribute(Scope scope, String name, Class<T> type,
            Annotation[] hint, boolean required,
            boolean convertNullToDefaultValueWhereTypeIsPrimitive)
            throws AttributeNotFoundRuntimeException {
        Class<?> componentType = ClassUtils.toComponentType(type);
        Object value = scope.getAttribute(name, componentType);
        if (required && value == null) {
            throw new AttributeNotFoundRuntimeException().setName(name)
                    .setType(componentType);
        }

        if (value != null && YmirContext.isUnderDevelopment()) {
            // 開発時はHotdeployのせいで見かけ上型が合わないことがありうる。
            // そのため開発時で見かけ上型が合わない場合はオブジェクトを再構築する。
            // なお、value自身がHotdeployClassLoader以外から読まれたコンテナ
            // クラスのインスタンスで、中身がHotdeployClassLoaderから読まれたクラス
            // のインスタンスである場合に適切にオブジェクトを再構築できるように、
            // 無条件にvalueをfit()に渡すようにしている。（[#YMIR-136]）
            value = hotdeployManager_.fit(value);
        }

        if (value == null && !convertNullToDefaultValueWhereTypeIsPrimitive) {
            return null;
        }

        return typeConversionManager_.convert(value, type, hint);
    }

    public ScopeMetaData getMetaData(Class<?> clazz) {
        ScopeMetaData metaData = metaDataMap_.get(clazz);
        if (metaData == null) {
            metaData = newInstance(clazz);
            metaDataMap_.put(clazz, metaData);
        }
        return metaData;
    }

    protected ScopeMetaData newInstance(Class<?> clazz) {
        return new ScopeMetaDataImpl(clazz, applicationManager_
                .findContextApplication().getS2Container(), annotationHandler_,
                this, typeConversionManager_);
    }
}
