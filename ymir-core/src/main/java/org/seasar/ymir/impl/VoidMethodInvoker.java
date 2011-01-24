package org.seasar.ymir.impl;

import java.lang.reflect.Method;

import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.WrappingRuntimeException;

/**
 * メソッド実行をしないことを表すMethodInvokerです。
 */
public class VoidMethodInvoker implements MethodInvoker {
    public static VoidMethodInvoker INSTANCE = new VoidMethodInvoker();

    public Method getMethod() {
        return null;
    }

    public Object[] getParameters() {
        return new Object[0];
    }

    public Class<? extends Object> getReturnType() {
        return Void.TYPE;
    }

    public Object invoke(Object component) {
        return invoke(component, getParameters());
    }

    public Object invoke(Object component, Object[] parameters)
            throws WrappingRuntimeException {
        return null;
    }

    public boolean shouldInvoke() {
        return false;
    }
}
