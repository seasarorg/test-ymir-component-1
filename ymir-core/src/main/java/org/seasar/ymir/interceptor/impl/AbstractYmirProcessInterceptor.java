package org.seasar.ymir.interceptor.impl;

import java.lang.reflect.Method;

import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;

public class AbstractYmirProcessInterceptor implements YmirProcessInterceptor {
    public Request filterRequest(Request request) {
        return request;
    }

    public Object filterComponent(Object component) {
        return component;
    }

    public void beginProcessingComponent(Object component) {
    }

    public Response beginInvokingAction(Object component, Method action,
            Request request) {
        return null;
    }

    public MethodInvoker aboutToInvokeAction(Object component, Request request,
            MethodInvoker methodInvoker) throws PermissionDeniedException {
        return methodInvoker;
    }
}
