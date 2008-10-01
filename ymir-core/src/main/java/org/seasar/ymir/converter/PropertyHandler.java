package org.seasar.ymir.converter;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface PropertyHandler {
    Class<?> getPropertyType();

    Method getReadMethod();

    Method getWriteMethod();

    PropertyDescriptor getPropertyDescriptor();

    void setProperty(Object value) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException;
}
