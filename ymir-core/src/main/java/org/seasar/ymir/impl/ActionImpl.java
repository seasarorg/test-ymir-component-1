package org.seasar.ymir.impl;

import java.lang.reflect.Method;

import org.seasar.ymir.Action;
import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.WrappingRuntimeException;

public class ActionImpl implements Action {
    private Object target_;

    private Class<?> targetClass_;

    private MethodInvoker methodInvoker_;

    public ActionImpl(Object target, Class<?> targetClass,
            MethodInvoker methodInvoker) {
        target_ = target;
        targetClass_ = targetClass;
        methodInvoker_ = methodInvoker;
    }

    @Override
    public String toString() {
        return targetClass_.getName() + "@" + System.identityHashCode(target_)
                + "#" + methodInvoker_.toString();
    }

    public MethodInvoker getMethodInvoker() {
        return methodInvoker_;
    }

    public Object getTarget() {
        return target_;
    }

    /**
     * @since 1.0.7
     */
    public Class<?> getTargetClass() {
        return targetClass_;
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
