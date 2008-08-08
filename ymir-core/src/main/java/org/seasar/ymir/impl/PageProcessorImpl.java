package org.seasar.ymir.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.log.Logger;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.PageMetaData;
import org.seasar.ymir.PageProcessor;
import org.seasar.ymir.Phase;
import org.seasar.ymir.PopulationMetaData;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.WrappingRuntimeException;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.scope.handler.ScopeAttributeHandler;

public class PageProcessorImpl implements PageProcessor {
    private TypeConversionManager typeConversionManager_;

    private final Logger logger_ = Logger.getLogger(getClass());

    @Binding(bindingType = BindingType.MUST)
    public void setTypeConversionManager(
            TypeConversionManager typeConversionManager) {
        typeConversionManager_ = typeConversionManager;
    }

    public void injectProperties(Object page, PageMetaData metaData,
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
                if (logger_.isDebugEnabled()) {
                    logger_.debug("Can't set property '" + name + "'", t);
                }
            }
        }
    }

    public void injectFormFileProperties(Object page, PageMetaData metaData,
            Map<String, FormFile[]> properties) {
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
                if (logger_.isDebugEnabled()) {
                    logger_.debug("Can't copy property '" + name + "'", t);
                }
            }
        }
    }

    public void populateScopeAttributes(Object page, PageMetaData metaData) {
        PopulationMetaData[] scopes = metaData.getPopulationMetaDatas();
        for (int i = 0; i < scopes.length; i++) {
            populateScopeAttributes(scopes[i]);
        }
    }

    protected void populateScopeAttributes(Object page,
            PopulationMetaData metaData, String actionName) {
        Scope scope = metaData.getScope();
        for (Iterator<String> itr = scope.getAttributeNames(); itr.hasNext();) {
            String name = itr.next();
            if (metaData.isProtected(name, actionName)) {
                continue;
            }
            try {
                typeConversionManager_.setProperty(page, name, scope
                        .getAttribute(name));
            } catch (Throwable t) {
                if (logger_.isDebugEnabled()) {
                    logger_.debug("Can't populate property '" + name + "'", t);
                }
            }
        }
    }

    public void injectScopeAttributes(Object page, PageMetaData metaData,
            String actionName) {
        ScopeAttributeHandler[] handlers = metaData
                .getInjectedScopeAttributeHandlers();
        for (int i = 0; i < handlers.length; i++) {
            if (handlers[i].isEnabled(actionName)) {
                handlers[i].injectTo(page);
            }
        }
    }

    public void outjectScopeAttributes(Object page, PageMetaData metaData,
            String actionName) {
        ScopeAttributeHandler[] attributes = metaData
                .getOutjectedScopeAttributeHandlers();
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].isEnabled(actionName)) {
                attributes[i].outjectFrom(page);
            }
        }
    }

    public void invokeMethods(Object page, PageMetaData metaData, Phase phase) {
        Method[] methods = metaData.getMethods(phase);
        if (methods != null) {
            for (int i = 0; i < methods.length; i++) {
                try {
                    methods[i].invoke(page, new Object[0]);
                } catch (IllegalArgumentException ex) {
                    throw new RuntimeException(
                            "Can't invoke method with parameters", ex);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                } catch (InvocationTargetException ex) {
                    throw new WrappingRuntimeException(ex.getCause());
                }
            }
        }
    }
}