package org.seasar.ymir.converter.impl;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.MappedPropertyDescriptor;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.seasar.ymir.converter.PropertyHandler;

public class BeanUtilsPropertyHandler implements PropertyHandler {
    private PropertyUtilsBean propertyUtilsBean_;

    private PropertyDescriptor propertyDescriptor_;

    private Object bean_;

    private String name_;

    private boolean indexed_;

    private boolean mapped_;

    public BeanUtilsPropertyHandler(PropertyUtilsBean propertyUtilsBean,
            PropertyDescriptor propertyDescriptor, Object bean, String name) {
        propertyUtilsBean_ = propertyUtilsBean;
        propertyDescriptor_ = propertyDescriptor;
        bean_ = bean;
        name_ = name;
        if (propertyDescriptor instanceof IndexedPropertyDescriptor) {
            indexed_ = true;
        } else if (propertyDescriptor instanceof MappedPropertyDescriptor) {
            mapped_ = true;
        }
    }

    public Class<?> getPropertyType() {
        if (indexed_) {
            return ((IndexedPropertyDescriptor) propertyDescriptor_)
                    .getIndexedPropertyType();
        } else if (mapped_) {
            return ((MappedPropertyDescriptor) propertyDescriptor_)
                    .getMappedPropertyType();
        } else {
            return propertyDescriptor_.getPropertyType();
        }
    }

    public PropertyDescriptor getPropertyDescriptor() {
        return propertyDescriptor_;
    }

    public Method getReadMethod() {
        if (indexed_) {
            return ((IndexedPropertyDescriptor) propertyDescriptor_)
                    .getIndexedReadMethod();
        } else if (mapped_) {
            return ((MappedPropertyDescriptor) propertyDescriptor_)
                    .getMappedReadMethod();
        } else {
            return propertyDescriptor_.getReadMethod();
        }
    }

    public Method getWriteMethod() {
        if (indexed_) {
            return ((IndexedPropertyDescriptor) propertyDescriptor_)
                    .getIndexedWriteMethod();
        } else if (mapped_) {
            return ((MappedPropertyDescriptor) propertyDescriptor_)
                    .getMappedWriteMethod();
        } else {
            return propertyDescriptor_.getWriteMethod();
        }
    }

    public void setProperty(Object value) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException {
        propertyUtilsBean_.setProperty(bean_, name_, value);
    }
}
