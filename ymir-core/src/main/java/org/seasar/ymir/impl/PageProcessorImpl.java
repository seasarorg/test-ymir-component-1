package org.seasar.ymir.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.log.Logger;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.PageProcessor;
import org.seasar.ymir.PageMetaData;
import org.seasar.ymir.Phase;
import org.seasar.ymir.ScopeAttribute;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.WrappingRuntimeException;

public class PageProcessorImpl implements PageProcessor {
    private TypeConversionManager typeConversionManager_;

    private final Logger logger_ = Logger.getLogger(getClass());

    @Binding(bindingType = BindingType.MUST)
    public void setTypeConversionManager(
            TypeConversionManager typeConversionManager) {
        typeConversionManager_ = typeConversionManager;
    }

    public void injectRequestParameters(Object page, PageMetaData metaData,
            Map<String, String[]> properties) {
        for (Iterator itr = properties.keySet().iterator(); itr.hasNext();) {
            String name = (String) itr.next();
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

    public void injectRequestFileParameters(Object page, PageMetaData metaData,
            Map<String, FormFile[]> properties) {
        for (Iterator itr = properties.keySet().iterator(); itr.hasNext();) {
            String name = (String) itr.next();
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

    public void injectContextAttributes(Object page, PageMetaData metaData,
            String actionName) {
        ScopeAttribute[] attributes = metaData.getInjectedScopeAttributes();
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].isEnable(actionName)) {
                attributes[i].injectTo(page);
            }
        }
    }

    public void outjectContextAttributes(Object page, PageMetaData metaData,
            String actionName) {
        ScopeAttribute[] attributes = metaData.getOutjectedScopeAttributes();
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].isEnable(actionName)) {
                attributes[i].outjectFrom(page);
            }
        }
    }

    public void invokeMethods(Object page, PageMetaData metaData, Phase phase) {
        Method[] methods = metaData.getMethods(phase);
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