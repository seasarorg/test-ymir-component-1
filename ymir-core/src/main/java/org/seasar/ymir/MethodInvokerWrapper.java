package org.seasar.ymir;

import java.lang.reflect.Method;

public class MethodInvokerWrapper implements MethodInvoker {
    protected MethodInvoker methodInvoker_;

    public MethodInvokerWrapper(MethodInvoker methodInvoker) {
        methodInvoker_ = methodInvoker;
    }

    public MethodInvoker getMethodInvoker() {
        return methodInvoker_;
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

    public final Object invoke(Object component) {
        return invoke(component, getParameters());
    }

    public Object invoke(Object component, Object[] parameters)
            throws WrappingRuntimeException {
        return methodInvoker_.invoke(component, parameters);
    }

    public boolean shouldInvoke() {
        return methodInvoker_.shouldInvoke();
    }
}
