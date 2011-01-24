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

    public MethodInvokerImpl(Method method, Object... parameters) {
        setMethod(method);
        setParameters(parameters);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(method_ != null ? method_.getName() : "(null)").append('(');
        if (parameters_ != null) {
            String delim = "";
            for (int i = 0; i < parameters_.length; i++) {
                sb.append(delim).append(parameters_[i]);
                delim = ", ";
            }
        }
        sb.append(')');
        return sb.toString();
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

    public final Object invoke(Object component) {
        return invoke(component, parameters_);
    }

    public Object invoke(Object component, Object[] parameters)
            throws WrappingRuntimeException {
        try {
            return method_.invoke(component, parameters);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            Throwable targetEx = ex.getTargetException();
            if (targetEx instanceof Error) {
                throw (Error) targetEx;
            } else if (targetEx instanceof RuntimeException) {
                throw (RuntimeException) targetEx;
            } else {
                throw new WrappingRuntimeException(targetEx);
            }
        }
    }

    public Class<? extends Object> getReturnType() {
        if (method_ != null) {
            return method_.getReturnType();
        } else {
            return null;
        }
    }

    public boolean shouldInvoke() {
        return method_ != null;
    }
}
