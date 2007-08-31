package org.seasar.ymir.impl;

import java.lang.reflect.Method;

import org.seasar.ymir.MethodInvoker;

/**
 * 指定された返り値を単にそのままinvoke呼び出しの返り値として返すようなMethodInvoker実装です。
 */
public class AsIsMethodInvoker implements MethodInvoker {
    private Object returnValue_;

    private Class<? extends Object> returnType_;

    public AsIsMethodInvoker(Object returnValue) {
        returnValue_ = returnValue;
        if (returnValue != null) {
            returnType_ = returnValue.getClass();
        } else {
            returnType_ = Object.class;
        }
    }

    public AsIsMethodInvoker(Object returnValue,
            Class<? extends Object> returnType) {
        returnValue_ = returnValue;
        returnType_ = returnType;
    }

    @Override
    public String toString() {
        return "(as is)" + String.valueOf(returnValue_);
    }

    public Method getMethod() {
        return null;
    }

    public Object[] getParameters() {
        return new Object[0];
    }

    public Class<? extends Object> getReturnType() {
        return returnType_;
    }

    public Object invoke(Object component) {
        return returnValue_;
    }

    public boolean shouldInvoke() {
        return true;
    }
}
