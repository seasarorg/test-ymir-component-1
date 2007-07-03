package org.seasar.ymir.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.WrappingRuntimeException;

public class MethodInvokerImpl implements MethodInvoker {
    private Method method_;

    private Object[] parameters_;

    public MethodInvokerImpl() {
    }

    public MethodInvokerImpl(Method method, Object[] parameters) {
        setMethod(method);
        setParameters(parameters);
    }

    public Method getMethod() {
        return method_;
    }

    public void setMethod(Method action) {
        method_ = action;
    }

    public Object[] getParameters() {
        return parameters_;
    }

    public void setParameters(Object[] parameters) {
        parameters_ = parameters;
    }

    public Object invoke(Object component) {
        try {
            return method_.invoke(component, parameters_);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new WrappingRuntimeException(ex.getCause() != null ? ex
                    .getCause() : ex);
        }
    }
}
