package org.seasar.ymir.impl;

import java.lang.reflect.Method;

import org.seasar.ymir.MethodInvoker;

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
        return null;
    }

    public boolean shouldInvoke() {
        return false;
    }
}
