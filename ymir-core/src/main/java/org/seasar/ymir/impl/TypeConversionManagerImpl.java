package org.seasar.ymir.impl;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.beanutils.FormFileArrayConverter;
import org.seasar.ymir.beanutils.FormFileConverter;

public class TypeConversionManagerImpl implements TypeConversionManager {
    private final ConvertUtilsBean convertUtilsBean_;

    private final PropertyUtilsBean propertyUtilsBean_;

    private final BeanUtilsBean beanUtilsBean_;

    public TypeConversionManagerImpl() {
        convertUtilsBean_ = new ConvertUtilsBean();
        convertUtilsBean_.register(new FormFileConverter(), FormFile.class);
        convertUtilsBean_.register(new FormFileArrayConverter(),
                FormFile[].class);
        propertyUtilsBean_ = new PropertyUtilsBean();
        beanUtilsBean_ = new BeanUtilsBean(convertUtilsBean_,
                propertyUtilsBean_);
        DisposableUtil.add(new Disposable() {
            public void dispose() {
                propertyUtilsBean_.clearDescriptors();
            }
        });
    }

    public void copyProperty(Object bean, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        if (propertyUtilsBean_.isWriteable(bean, name)) {
            beanUtilsBean_.copyProperty(bean, name, value);
        }
    }

    public void setProperty(Object bean, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        if (propertyUtilsBean_.isWriteable(bean, name)) {
            beanUtilsBean_.setProperty(bean, name, value);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T convert(String value, Class<T> type) {
        return (T) convertUtilsBean_.convert(value, type);
    }
}
