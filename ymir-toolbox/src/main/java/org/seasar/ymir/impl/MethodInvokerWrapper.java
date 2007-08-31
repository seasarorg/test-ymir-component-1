package org.seasar.ymir.impl;

import java.lang.reflect.Method;

import org.seasar.ymir.MethodInvoker;

public class MethodInvokerWrapper implements MethodInvoker {
    protected MethodInvoker methodInvoker_;

    public MethodInvokerWrapper(MethodInvoker methodInvoker) {
        methodInvoker_ = methodInvoker;
    }

    public Method getMethod() {
        return methodInvoker_.getMethod();
    }

    public Object[] getParameters() {
        return methodInvoker_.getParameters();
    }

    public Class<? extends Object> getReturnType() {
        return methodInvoker_.getReturnType();
    }

    public Object invoke(Object component) {
        return methodInvoker_.invoke(component);
    }

    public boolean shouldInvoke() {
        return methodInvoker_.shouldInvoke();
    }
}
