package org.seasar.ymir;

import java.lang.reflect.Method;

public interface ActionManager {
    Action newAction(Object page, Class<?> pageClass, Method method);

    Action newAction(Object page, Class<?> pageClass, Method method,
            Object[] extendedParams);

    MethodInvoker newMethodInvoker(Class<?> pageClass, Method method);

    MethodInvoker newMethodInvoker(Class<?> pageClass, Method method,
            Object[] extendedParams);

    Action newAction(Object page, MethodInvoker methodInvoker);

    Action newVoidAction(Object page);
}
