package org.seasar.ymir.impl;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Action;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.Response;
import org.seasar.ymir.response.PassthroughResponse;
import org.seasar.ymir.response.constructor.ResponseConstructor;
import org.seasar.ymir.response.constructor.ResponseConstructorSelector;
import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.util.ClassUtils;

public class ActionManagerImpl implements ActionManager {
    private ResponseConstructorSelector responseConstructorSelector_;

    private ScopeManager scopeManager_;

    private static final Log log_ = LogFactory.getLog(ActionManagerImpl.class);

    @Binding(bindingType = BindingType.MUST)
    public void setResponseConstructorSelector(
            ResponseConstructorSelector responseConstructorSelector) {
        responseConstructorSelector_ = responseConstructorSelector;
    }

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

    public Response invokeAction(Action action) {
        Response response = new PassthroughResponse();

        if (action != null && action.shouldInvoke()) {
            if (log_.isDebugEnabled()) {
                log_.debug("INVOKE: "
                        + ClassUtils.getPrettyName(action.getTarget()) + "#"
                        + action.getMethodInvoker());
            }
            response = constructResponse(action.getTarget(), action
                    .getReturnType(), action.invoke());
        }

        return response;
    }

    @SuppressWarnings("unchecked")
    public Response constructResponse(Object page, Class<?> returnType,
            Object returnValue) {
        ResponseConstructor<?> constructor = responseConstructorSelector_
                .getResponseConstructor(returnType);
        if (constructor == null) {
            throw new ComponentNotFoundRuntimeException(
                    "Can't find ResponseConstructor for type '" + returnType
                            + "' in ResponseConstructorSelector");
        }

        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            if (page != null) {
                // XXX request.getComponentClass().getClassLoader()にすべきか？
                Thread.currentThread().setContextClassLoader(
                        page.getClass().getClassLoader());
            }
            return ((ResponseConstructor<Object>) constructor)
                    .constructResponse(page, returnValue);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }
}
