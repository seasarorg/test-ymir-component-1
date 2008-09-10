package org.seasar.ymir.impl;

import java.lang.reflect.Method;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Action;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.ComponentMetaDataFactory;
import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.scope.handler.ScopeAttributeResolver;

public class ActionManagerImpl implements ActionManager {
    private ComponentMetaDataFactory componentMetaDataFactory_;

    private TypeConversionManager typeConversionManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setComponentMetaDataFactory(
            ComponentMetaDataFactory componentMetaDataFactory) {
        componentMetaDataFactory_ = componentMetaDataFactory;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setTypeConversionManager(
            TypeConversionManager typeConversionManager) {
        typeConversionManager_ = typeConversionManager;
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
        Class<?>[] types = method.getParameterTypes();
        ScopeAttributeResolver[] resolvers = componentMetaDataFactory_
                .getInstance(pageClass)
                .getScopeAttributeResolversForParameters(method);
        Object[] params = new Object[types.length];
        int buttonParamsIdx = 0;
        for (int i = 0; i < types.length; i++) {
            if (resolvers[i] != null) {
                params[i] = resolvers[i].getValue();
            } else {
                Object value = null;
                if (buttonParamsIdx < extendedParams.length) {
                    value = extendedParams[buttonParamsIdx++];
                }
                params[i] = typeConversionManager_.convert(value, types[i]);
            }
        }
        return new MethodInvokerImpl(method, params);
    }

    public Action newAction(Object page, MethodInvoker methodInvoker) {
        return new ActionImpl(page, methodInvoker);
    }

    public Action newVoidAction(Object page) {
        return newAction(page, VoidMethodInvoker.INSTANCE);
    }
}
