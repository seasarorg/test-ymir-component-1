package org.seasar.ymir.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ComponentMetaData;
import org.seasar.ymir.ComponentMetaDataFactory;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageProcessor;
import org.seasar.ymir.Phase;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.WrappingRuntimeException;
import org.seasar.ymir.scope.handler.ScopeAttributeHandler;

public class PageProcessorImpl implements PageProcessor {
    private ComponentMetaDataFactory componentMetaDataFactory_;

    private TypeConversionManager typeConversionManager_;

    private static final Log log_ = LogFactory.getLog(PageProcessorImpl.class);

    @Binding(bindingType = BindingType.MUST)
    public void setComponentMetaDataFactory(
            ComponentMetaDataFactory componentMetaDataFactory) {
        componentMetaDataFactory_ = componentMetaDataFactory;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setTypeConversionManager(
            TypeConversionManager typeConversionManager) {
        typeConversionManager_ = typeConversionManager;
    }

    public void injectProperties(Object page, ComponentMetaData metaData,
            Map<String, String[]> properties) {
        if (properties == null) {
            return;
        }

        for (Iterator<String> itr = properties.keySet().iterator(); itr
                .hasNext();) {
            String name = itr.next();
            if (name == null || metaData.isProtected(name)) {
                continue;
            }
            try {
                typeConversionManager_.setProperty(page, name, properties
                        .get(name));
            } catch (Throwable t) {
                if (log_.isDebugEnabled()) {
                    log_.debug("Can't set property '" + name + "'", t);
                }
            }
        }
    }

    public void injectFormFileProperties(Object page,
            ComponentMetaData metaData, Map<String, FormFile[]> properties) {
        if (properties == null) {
            return;
        }

        for (Iterator<String> itr = properties.keySet().iterator(); itr
                .hasNext();) {
            String name = itr.next();
            if (name == null || metaData.isProtected(name)) {
                continue;
            }
            try {
                typeConversionManager_.copyProperty(page, name, properties
                        .get(name));
            } catch (Throwable t) {
                if (log_.isDebugEnabled()) {
                    log_.debug("Can't copy property '" + name + "'", t);
                }
            }
        }
    }

    public void populateScopeAttributes(PageComponent pageComponent,
            String actionName) {
        ComponentMetaData metaData = componentMetaDataFactory_
                .getInstance(pageComponent.getPageClass());
        ScopeAttributeHandler[] handlers = metaData
                .getPopulatedScopeAttributeHandlers();
        for (int i = 0; i < handlers.length; i++) {
            handlers[i].injectTo(pageComponent.getPage(), actionName);
        }
    }

    public void injectScopeAttributes(PageComponent pageComponent,
            String actionName) {
        ComponentMetaData metaData = componentMetaDataFactory_
                .getInstance(pageComponent.getPageClass());
        ScopeAttributeHandler[] handlers = metaData
                .getInjectedScopeAttributeHandlers();
        for (int i = 0; i < handlers.length; i++) {
            handlers[i].injectTo(pageComponent.getPage(), actionName);
        }
    }

    public void outjectScopeAttributes(PageComponent pageComponent,
            String actionName) {
        ComponentMetaData metaData = componentMetaDataFactory_
                .getInstance(pageComponent.getPageClass());
        ScopeAttributeHandler[] attributes = metaData
                .getOutjectedScopeAttributeHandlers();
        for (int i = 0; i < attributes.length; i++) {
            attributes[i].outjectFrom(pageComponent.getPage(), actionName);
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