package org.seasar.ymir.impl;

import java.lang.reflect.Method;

import org.seasar.ymir.Action;
import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.WrappingRuntimeException;

public class ActionImpl implements Action {
    private Object target_;

    private MethodInvoker methodInvoker_;

    public ActionImpl(Object target, MethodInvoker methodInvoker) {
        target_ = target;
        methodInvoker_ = methodInvoker;
    }

    @Override
    public String toString() {
        return target_.getClass().getName() + "@"
                + System.identityHashCode(target_) + "#"
                + methodInvoker_.toString();
    }

    public MethodInvoker getMethodInvoker() {
        return methodInvoker_;
    }

    public Object getTarget() {
        return target_;
    }

    public String getName() {
        Method method = methodInvoker_.getMethod();
        if (method != null) {
            return method.getName();
        } else {
            return null;
        }
    }

    public Object invoke() throws WrappingRuntimeException {
        return methodInvoker_.invoke(target_);
    }

    public boolean shouldInvoke() {
        return methodInvoker_.shouldInvoke();
    }

    public Class<? extends Object> getReturnType() {
        return methodInvoker_.getReturnType();
    }
}
