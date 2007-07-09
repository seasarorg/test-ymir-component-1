package org.seasar.ymir;

import java.lang.reflect.Method;

public interface MethodInvoker {
    Method getMethod();

    Object[] getParameters();

    Object invoke(Object component);

    Class<? extends Object> getReturnType();

    boolean shouldInvoke();
}
