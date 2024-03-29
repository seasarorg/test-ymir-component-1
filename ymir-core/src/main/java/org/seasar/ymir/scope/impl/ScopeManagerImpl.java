package org.seasar.ymir.scope.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.cache.CacheManager;
import org.seasar.ymir.converter.PropertyHandler;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.converter.annotation.TypeConversionHint;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.scope.AttributeNotFoundRuntimeException;
import org.seasar.ymir.scope.PopulationFailureException;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.scope.ScopeMetaData;
import org.seasar.ymir.scope.handler.ScopeAttributeInjector;
import org.seasar.ymir.scope.handler.ScopeAttributeOutjector;
import org.seasar.ymir.scope.handler.ScopeAttributePopulator;
import org.seasar.ymir.scope.handler.ScopeAttributeResolver;
import org.seasar.ymir.util.ClassUtils;

public class ScopeManagerImpl implements ScopeManager {
    private static final Log log_ = LogFactory.getLog(ScopeManagerImpl.class);

    private ActionManager actionManager_;

    private ApplicationManager applicationManager_;

    private AnnotationHandler annotationHandler_;

    private HotdeployManager hotdeployManager_;

    private TypeConversionManager typeConversionManager_;

    private Map<Class<?>, ScopeMetaData> metaDataMap_;

    @Binding(bindingType = BindingType.MUST)
    public void setActionManager(ActionManager actionManager) {
        actionManager_ = actionManager;
    }

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

    /**
     * 指定されたクラスについてのスコープ関連のメタ情報を保持する{@link ScopeMetaData}オブジェクトを返します。
     * 
     * @param clazz クラス。nullを指定してはいけません。
     * @return {@link ScopeMetaData}オブジェクト。
     * @see ScopeMetaData
     */
    protected ScopeMetaData getMetaData(Class<?> clazz) {
        ScopeMetaData metaData = metaDataMap_.get(clazz);
        if (metaData == null) {
            metaData = newInstance(clazz);
            metaDataMap_.put(clazz, metaData);
        }
        return metaData;
    }

    protected ScopeMetaData newInstance(Class<?> clazz) {
        return new ScopeMetaDataImpl(clazz, applicationManager_
                .findContextApplication().getS2Container(), actionManager_,
                annotationHandler_, applicationManager_, this,
                typeConversionManager_);
    }

    public void populateScopeAttributes(PageComponent pageComponent,
            String actionName) {
        ScopeMetaData metaData = getMetaData(pageComponent.getPageClass());
        ScopeAttributePopulator[] populators = metaData
                .getScopeAttributePopulators();
        for (int i = 0; i < populators.length; i++) {
            populators[i].populateTo(pageComponent.getPage(), actionName);
        }
    }

    public void injectScopeAttributes(PageComponent pageComponent,
            String actionName) {
        ScopeMetaData metaData = getMetaData(pageComponent.getPageClass());
        ScopeAttributeInjector[] injectors = metaData
                .getScopeAttributeInjectors();
        for (int i = 0; i < injectors.length; i++) {
            injectors[i].injectTo(pageComponent.getPage(), actionName);
        }
    }

    public void outjectScopeAttributes(PageComponent pageComponent,
            String actionName) {
        ScopeMetaData metaData = getMetaData(pageComponent.getPageClass());
        ScopeAttributeOutjector[] outjectors = metaData
                .getScopeAttributeOutjectors();
        for (int i = 0; i < outjectors.length; i++) {
            outjectors[i].outjectFrom(pageComponent.getPage(), actionName);
        }
    }

    public Object[] resolveParameters(Class<?> pageClass, Method method,
            Object[] extendedParams) {
        Class<?>[] types = method.getParameterTypes();
        ScopeAttributeResolver[] resolvers = getMetaData(pageClass)
                .getScopeAttributeResolversForParameters(method);
        Object[] params = new Object[types.length];
        int extendedParamsIdx = 0;
        for (int i = 0; i < types.length; i++) {
            if (resolvers[i] != null) {
                params[i] = resolvers[i].getValue();
            } else {
                Object value = null;
                if (extendedParamsIdx < extendedParams.length) {
                    value = extendedParams[extendedParamsIdx++];
                }
                params[i] = typeConversionManager_.convert(value, types[i],
                        annotationHandler_.getMarkedParameterAnnotations(
                                method, i, TypeConversionHint.class));
            }
        }
        return params;
    }

    public void populate(Object bean, Map<String, ?> parameterMap)
            throws PopulationFailureException {
        populate0(bean, parameterMap, false);
    }

    public void populateQuietly(Object bean,
            Map<String, ? extends Object> parameterMap) {
        try {
            populate0(bean, parameterMap, true);
        } catch (PopulationFailureException ex) {
            throw new RuntimeException("Can't happen!", ex);
        }
    }

    protected void populate0(Object bean, Map<String, ?> parameterMap,
            boolean quietly) throws PopulationFailureException {
        if (bean == null || parameterMap == null) {
            return;
        }

        for (Map.Entry<String, ?> entry : parameterMap.entrySet()) {
            populate0(bean, entry.getKey(), entry.getValue(), quietly);
        }
    }

    public void populate(Object bean, String name, Object value)
            throws PopulationFailureException {
        populate0(bean, name, value, false);
    }

    public void populateQuietly(Object bean, String name, Object value) {
        try {
            populate0(bean, name, value, true);
        } catch (PopulationFailureException ex) {
            throw new RuntimeException("Can't happen!", ex);
        }
    }

    protected void populate0(Object bean, String name, Object value,
            boolean quietly) throws PopulationFailureException {
        PropertyHandler handler = typeConversionManager_.getPropertyHandler(
                bean, name);
        if (handler == null || handler.getWriteMethod() == null) {
            return;
        }

        Object converted = typeConversionManager_.convert(value, handler
                .getPropertyType(), annotationHandler_.getMarkedAnnotations(
                handler.getWriteMethod(), TypeConversionHint.class));

        try {
            handler.setProperty(converted);
        } catch (Throwable t) {
            if (log_.isDebugEnabled()) {
                log_.debug("Can't populate: property name=" + name + ", value="
                        + value + ", write method=" + handler.getWriteMethod(),
                        t);
            }
            if (!quietly) {
                throw new PopulationFailureException(name, value, handler, t);
            }
        }
    }
}
