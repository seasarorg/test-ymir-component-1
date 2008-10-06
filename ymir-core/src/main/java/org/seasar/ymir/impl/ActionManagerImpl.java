package org.seasar.ymir.impl;

import java.lang.reflect.Method;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Action;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.scope.ScopeManager;

public class ActionManagerImpl implements ActionManager {
    private ScopeManager scopeManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setScopeManager(ScopeManager scopeManager) {
        scopeManager_ = scopeManager;
    }

    public Action newAction(Object page, Class<?> pageClass, Method method) {
        return newAction(page, pageClass, method, new Object[0]);
    }

    public Action newAction(Object page, Class<?> pageClass, Method method,
            Object[] extendedParams) {
        return new ActionImpl(page, newMethodInvoker(pageClass, method,
                extendedParams));
    }

    public MethodInvoker newMethodInvoker(Class<?> pageClass, Method method) {
        return newMethodInvoker(pageClass, method, new Object[0]);
    }

    public MethodInvoker newMethodInvoker(Class<?> pageClass, Method method,
            Object[] extendedParams) {
        return new MethodInvokerImpl(method, scopeManager_.resolveParameters(
                pageClass, method, extendedParams));
    }

    public Action newAction(Object page, MethodInvoker methodInvoker) {
        return new ActionImpl(page, methodInvoker);
    }

    public Action newVoidAction(Object page) {
        return newAction(page, VoidMethodInvoker.INSTANCE);
    }
}
