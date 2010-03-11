package org.seasar.ymir.converter.impl;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.MappedPropertyDescriptor;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.seasar.ymir.converter.PropertyHandler;

public class BeanUtilsPropertyHandler implements PropertyHandler {
    private static final String INDEXED_DELIM2 = "]";

    private static final String MAPPED_DELIM2 = ")";

    private PropertyUtilsBean propertyUtilsBean_;

    private PropertyDescriptor propertyDescriptor_;

    private Object bean_;

    private String name_;

    private boolean indexed_;

    private boolean mapped_;

    private Class<?> propertyType;

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
        } else {
            if (name.endsWith(INDEXED_DELIM2)) {
                Class<?> type = propertyDescriptor.getPropertyType();
                if (type.isArray()) {
                    propertyType = type.getComponentType();
                } else if (List.class.isAssignableFrom(type)) {
                    Method method = propertyDescriptor.getReadMethod();
                    if (method != null) {
                        Type returnType = method.getGenericReturnType();
                        if (returnType instanceof ParameterizedType) {
                            propertyType = toClass(((ParameterizedType) returnType)
                                    .getActualTypeArguments()[0]);
                        } else {
                            propertyType = Object.class;
                        }
                    }
                }
            } else if (name.endsWith(MAPPED_DELIM2)) {
                Class<?> type = propertyDescriptor.getPropertyType();
                if (Map.class.isAssignableFrom(type)) {
                    Method method = propertyDescriptor.getReadMethod();
                    if (method != null) {
                        Type returnType = method.getGenericReturnType();
                        if (returnType instanceof ParameterizedType) {
                            propertyType = toClass(((ParameterizedType) returnType)
                                    .getActualTypeArguments()[1]);
                        } else {
                            propertyType = Object.class;
                        }
                    }
                }
            }
        }
    }

    Class<?> toClass(Type type) {
        if (type == null) {
            return null;
        }
        if (type instanceof ParameterizedType) {
            return toClass(((ParameterizedType) type).getRawType());
        } else if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else {
            return Object.class;
        }
    }

    public Class<?> getPropertyType() {
        if (propertyType != null) {
            return propertyType;
        } else {
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

    public Object getProperty() throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        return propertyUtilsBean_.getProperty(bean_, name_);
    }
}
