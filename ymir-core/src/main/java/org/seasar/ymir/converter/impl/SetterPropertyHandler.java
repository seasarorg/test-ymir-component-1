package org.seasar.ymir.converter.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.seasar.ymir.converter.PropertyHandler;

public class SetterPropertyHandler implements PropertyHandler {
    private Object obj_;

    private Method method_;

    public SetterPropertyHandler(Object obj, Method method) {
        obj_ = obj;
        method_ = method;
    }

    public PropertyDescriptor getPropertyDescriptor() {
        throw new UnsupportedOperationException();
    }

    public Class<?> getPropertyType() {
        return method_.getParameterTypes()[0];
    }

    public Method getReadMethod() {
        return null;
    }

    public Method getWriteMethod() {
        return method_;
    }

    public void setProperty(Object value) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException {
        method_.invoke(obj_, new Object[] { value });
    }

    public Object getProperty() throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        return null;
    }
}
