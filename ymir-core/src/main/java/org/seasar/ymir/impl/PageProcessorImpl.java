package org.seasar.ymir.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ComponentMetaData;
import org.seasar.ymir.ComponentMetaDataFactory;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageProcessor;
import org.seasar.ymir.Phase;
import org.seasar.ymir.WrappingRuntimeException;
import org.seasar.ymir.scope.handler.ScopeAttributeInjector;
import org.seasar.ymir.scope.handler.ScopeAttributeOutjector;
import org.seasar.ymir.scope.handler.ScopeAttributePopulator;

public class PageProcessorImpl implements PageProcessor {
    private ComponentMetaDataFactory componentMetaDataFactory_;

    private static final Log log_ = LogFactory.getLog(PageProcessorImpl.class);

    @Binding(bindingType = BindingType.MUST)
    public void setComponentMetaDataFactory(
            ComponentMetaDataFactory componentMetaDataFactory) {
        componentMetaDataFactory_ = componentMetaDataFactory;
    }

    public void populateScopeAttributes(PageComponent pageComponent,
            String actionName) {
        ComponentMetaData metaData = componentMetaDataFactory_
                .getInstance(pageComponent.getPageClass());
        ScopeAttributePopulator[] populators = metaData
                .getScopeAttributePopulators();
        for (int i = 0; i < populators.length; i++) {
            populators[i].populateTo(pageComponent.getPage(), actionName);
        }
    }

    public void injectScopeAttributes(PageComponent pageComponent,
            String actionName) {
        ComponentMetaData metaData = componentMetaDataFactory_
                .getInstance(pageComponent.getPageClass());
        ScopeAttributeInjector[] injectors = metaData
                .getScopeAttributeInjectors();
        for (int i = 0; i < injectors.length; i++) {
            injectors[i].injectTo(pageComponent.getPage(), actionName);
        }
    }

    public void outjectScopeAttributes(PageComponent pageComponent,
            String actionName) {
        ComponentMetaData metaData = componentMetaDataFactory_
                .getInstance(pageComponent.getPageClass());
        ScopeAttributeOutjector[] outjectors = metaData
                .getScopeAttributeOutjectors();
        for (int i = 0; i < outjectors.length; i++) {
            outjectors[i].outjectFrom(pageComponent.getPage(), actionName);
        }
    }

    public void invokeMethods(PageComponent pageComponent, Phase phase) {
        ComponentMetaData metaData = componentMetaDataFactory_
                .getInstance(pageComponent.getPageClass());
        Method[] methods = metaData.getMethods(phase);
        if (methods != null) {
            for (int i = 0; i < methods.length; i++) {
                try {
                    methods[i].invoke(pageComponent.getPage(), new Object[0]);
                } catch (IllegalArgumentException ex) {
                    throw new RuntimeException(
                            "Can't invoke method with parameters", ex);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                } catch (InvocationTargetException ex) {
                    throw new WrappingRuntimeException(ex.getTargetException());
                }
            }
        }
    }
}