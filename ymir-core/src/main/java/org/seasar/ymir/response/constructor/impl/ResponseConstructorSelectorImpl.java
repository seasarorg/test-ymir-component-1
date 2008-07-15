package org.seasar.ymir.response.constructor.impl;

import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.response.constructor.ResponseConstructor;
import org.seasar.ymir.response.constructor.ResponseConstructorSelector;

public class ResponseConstructorSelectorImpl implements
        ResponseConstructorSelector {
    private Map<Class<?>, ResponseConstructor<?>> constructorMap_ = new HashMap<Class<?>, ResponseConstructor<?>>();

    public boolean hasResponseConstructor(Class<?> type) {
        return constructorMap_.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    public <T> ResponseConstructor<T> getResponseConstructor(Class<T> type) {
        ResponseConstructor<?> constructor = constructorMap_.get(type);
        if (constructor != null) {
            return (ResponseConstructor<T>) constructor;
        } else {
            throw new RuntimeException(
                    "ResponseConstructor does not exist for class: " + type);
        }
    }

    public void add(ResponseConstructor<?> constructor) {
        constructorMap_.put(constructor.getTargetClass(), constructor);
    }

    @Binding(value = "@org.seasar.ymir.util.ContainerUtils@findAllComponents(container, @org.seasar.ymir.response.constructor.ResponseConstructor@class)", bindingType = BindingType.MUST)
    public void setResponseConstructors(ResponseConstructor<?>[] constructors) {
        for (int i = 0; i < constructors.length; i++) {
            add(constructors[i]);
        }
    }
}