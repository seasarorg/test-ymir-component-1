package org.seasar.ymir.impl;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.seasar.cms.pluggable.hotdeploy.AbstractHotdeployEventListener;
import org.seasar.cms.pluggable.util.HotdeployEventUtils;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.beanutils.FormFileArrayConverter;
import org.seasar.ymir.beanutils.FormFileConverter;

public class BeanUtilsTypeConversionManager implements TypeConversionManager {
    private final ConvertUtilsBean convertUtilsBean_;

    private final PropertyUtilsBean propertyUtilsBean_;

    private final BeanUtilsBean beanUtilsBean_;

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
    public <T> T convert(String value, Class<T> type) {
        return (T) convertUtilsBean_.convert(value, type);
    }

    @SuppressWarnings("unchecked")
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

    public String convert(Object value) {
        return convertUtilsBean_.convert(value);
    }
}
