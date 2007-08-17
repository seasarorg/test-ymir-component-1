package org.seasar.ymir.impl;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.PageProcessor;
import org.seasar.ymir.PagePropertyMetaData;
import org.seasar.ymir.ScopeAttribute;
import org.seasar.ymir.beanutils.FormFileArrayConverter;
import org.seasar.ymir.beanutils.FormFileConverter;

public class PageProcessorImpl implements PageProcessor {
    private final PropertyUtilsBean propertyUtilsBean_ = new PropertyUtilsBean();

    private final BeanUtilsBean beanUtilsBean_;

    private final Logger logger_ = Logger.getLogger(getClass());

    public PageProcessorImpl() {
        ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
        convertUtilsBean.register(new FormFileConverter(), FormFile.class);
        convertUtilsBean.register(new FormFileArrayConverter(),
                FormFile[].class);
        beanUtilsBean_ = new BeanUtilsBean(convertUtilsBean, propertyUtilsBean_);
        DisposableUtil.add(new Disposable() {
            public void dispose() {
                propertyUtilsBean_.clearDescriptors();
            }
        });
    }

    public void injectRequestParameters(Object page,
            PagePropertyMetaData metaData, Map<String, String[]> properties) {
        for (Iterator itr = properties.keySet().iterator(); itr.hasNext();) {
            String name = (String) itr.next();
            if (name == null || metaData.isProtected(name)) {
                continue;
            }
            try {
                beanUtilsBean_.setProperty(page, name, properties.get(name));
            } catch (Throwable t) {
                if (logger_.isDebugEnabled()) {
                    logger_.debug("Can't populate property '" + name + "'", t);
                }
            }
        }
    }

    public void injectRequestFileParameters(Object page,
            PagePropertyMetaData metaData, Map<String, FormFile[]> properties) {
        for (Iterator itr = properties.keySet().iterator(); itr.hasNext();) {
            String name = (String) itr.next();
            if (name == null || metaData.isProtected(name)) {
                continue;
            }
            if (beanUtilsBean_.getPropertyUtils().isWriteable(page, name)) {
                try {
                    beanUtilsBean_.copyProperty(page, name, properties
                            .get(name));
                } catch (Throwable t) {
                    if (logger_.isDebugEnabled()) {
                        logger_.debug("Can't copy property '" + name + "'", t);
                    }
                }
            }
        }
    }

    public void injectContextAttributes(Object page, String actionName,
            PagePropertyMetaData metaData) {
        ScopeAttribute[] attributes = metaData.getInjectedScopeAttributes();
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].isEnable(actionName)) {
                attributes[i].injectTo(page);
            }
        }
    }

    public void outjectContextAttributes(Object page, String actionName,
            PagePropertyMetaData metaData) {
        ScopeAttribute[] attributes = metaData.getOutjectedScopeAttributes();
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].isEnable(actionName)) {
                attributes[i].outjectFrom(page);
            }
        }
    }
}