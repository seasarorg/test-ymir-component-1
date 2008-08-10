package org.seasar.ymir.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.cms.pluggable.hotdeploy.AbstractHotdeployEventListener;
import org.seasar.cms.pluggable.util.HotdeployEventUtils;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.PropertyHandler;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.beanutils.FormFileArrayConverter;
import org.seasar.ymir.beanutils.FormFileConverter;

public class BeanUtilsTypeConversionManager implements TypeConversionManager {
    private final ConvertUtilsBean convertUtilsBean_;

    private final PropertyUtilsBean propertyUtilsBean_;

    private final BeanUtilsBean beanUtilsBean_;

    private static final Log log_ = LogFactory
            .getLog(BeanUtilsTypeConversionManager.class);

    public BeanUtilsTypeConversionManager() {
        convertUtilsBean_ = prepare(newConvertUtilsBean());
        propertyUtilsBean_ = prepare(newPropertyUtilsBean());
        beanUtilsBean_ = prepare(newBeanUtilsBean(convertUtilsBean_,
                propertyUtilsBean_));

        HotdeployEventUtils.add(new AbstractHotdeployEventListener() {
            public void stop() {
                propertyUtilsBean_.clearDescriptors();
            }
        });
    }

    protected ConvertUtilsBean newConvertUtilsBean() {
        return new ConvertUtilsBean();
    }

    protected ConvertUtilsBean prepare(ConvertUtilsBean convertUtilsBean) {
        convertUtilsBean.register(new FormFileConverter(), FormFile.class);
        convertUtilsBean.register(new FormFileArrayConverter(),
                FormFile[].class);
        return convertUtilsBean;
    }

    protected PropertyUtilsBean newPropertyUtilsBean() {
        return new PropertyUtilsBean();
    }

    protected PropertyUtilsBean prepare(PropertyUtilsBean propertyUtilsBean) {
        return propertyUtilsBean;
    }

    protected BeanUtilsBean newBeanUtilsBean(ConvertUtilsBean convertUtilsBean,
            PropertyUtilsBean propertyUtilsBean) {
        return new BeanUtilsBean(convertUtilsBean, propertyUtilsBean);
    }

    protected BeanUtilsBean prepare(BeanUtilsBean beanUtilsBean) {
        return beanUtilsBean;
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
    @Deprecated
    public <T> T convert(String value, Class<T> type) {
        return (T) convertUtilsBean_.convert(value, type);
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    public <T> T[] convert(String[] values, Class<T> type) {
        if (type.isArray()) {
            // ConvertUtilsBean#convert(String[], Class)はClassがArrayの場合
            // Class#getComponentType()に関してconvertしてしまうので、返り値が
            // 引数Classの配列にならない。その差異を吸収するためにこうしている。
            T[] array = (T[]) Array.newInstance(type, 1);
            array[0] = (T) convertUtilsBean_.convert(values, type);
            return array;
        } else {
            return (T[]) convertUtilsBean_.convert(values, type);
        }
    }

    @Deprecated
    public String convert(Object value) {
        return convertUtilsBean_.convert(value);
    }

    @SuppressWarnings("unchecked")
    public <T> T convert(Object value, Class<T> type) {
        if (value == null) {
            return convert((String) null, type);
        }
        Class<?> clazz = value.getClass();
        boolean isArray = clazz.isArray();
        boolean typeIsArray = type.isArray();
        Class<?> typeComponentType = typeIsArray ? type.getComponentType()
                : type;
        if (isArray) {
            if (typeIsArray) {
                Object[] converted = (Object[]) Array.newInstance(
                        typeComponentType, Array.getLength(value));
                for (int i = 0; i < converted.length; i++) {
                    converted[i] = convertComponent(Array.get(value, i),
                            typeComponentType);
                }
                return (T) converted;
            } else if (Array.getLength(value) > 0) {
                return (T) convertComponent(Array.get(value, 0),
                        typeComponentType);
            } else {
                return (T) convertComponent(null, typeComponentType);
            }
        } else {
            if (typeIsArray) {
                Object[] converted = (Object[]) Array.newInstance(
                        typeComponentType, 1);
                converted[0] = convertComponent(value, typeComponentType);
                return (T) converted;
            } else {
                return (T) convertComponent(value, typeComponentType);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T convertComponent(Object value, Class<T> componentType) {
        if (value == null || value instanceof String) {
            return convertStringTo((String) value, componentType);
        } else if (componentType.isAssignableFrom(value.getClass())) {
            return (T) value;
        } else {
            return convertStringTo(convertToString(value), componentType);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T convertStringTo(String value, Class<T> type) {
        return (T) convertUtilsBean_.convert(value, type);
    }

    protected String convertToString(Object value) {
        return convertUtilsBean_.convert(value);
    }

    public PropertyHandler getPropertyHandler(Object bean, String name) {
        PropertyDescriptor pd;
        try {
            pd = propertyUtilsBean_.getPropertyDescriptor(bean, name);
        } catch (IllegalArgumentException ex) {
            if (log_.isDebugEnabled()) {
                log_.debug("Can't get PropertyDescriptor: beanClass="
                        + bean.getClass() + ", name=" + name, ex);
            }
            pd = null;
        } catch (IllegalAccessException ex) {
            if (log_.isDebugEnabled()) {
                log_.debug("Can't get PropertyDescriptor: beanClass="
                        + bean.getClass() + ", name=" + name, ex);
            }
            pd = null;
        } catch (InvocationTargetException ex) {
            if (log_.isDebugEnabled()) {
                log_.debug("Can't get PropertyDescriptor: beanClass="
                        + bean.getClass() + ", name=" + name, ex);
            }
            pd = null;
        } catch (NoSuchMethodException ex) {
            if (log_.isDebugEnabled()) {
                log_.debug("Can't get PropertyDescriptor: beanClass="
                        + bean.getClass() + ", name=" + name, ex);
            }
            pd = null;
        }

        if (pd == null) {
            return null;
        } else {
            return new BeanUtilsPropertyHandler(propertyUtilsBean_, pd, bean,
                    name);
        }
    }
}
