package org.seasar.ymir;

import java.lang.reflect.InvocationTargetException;

public interface TypeConversionManager {
    void setProperty(Object bean, String name, Object value)
            throws IllegalAccessException, InvocationTargetException;

    void copyProperty(Object bean, String name, Object value)
            throws IllegalAccessException, InvocationTargetException;

    <T> T convert(String value, Class<T> type);

    String convert(Object value);
}
