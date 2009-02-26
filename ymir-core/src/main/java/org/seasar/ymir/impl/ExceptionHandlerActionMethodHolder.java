package org.seasar.ymir.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.seasar.ymir.MethodHolder;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.handler.annotation.ExceptionHandler;
import org.seasar.ymir.util.ClassUtils;

class ExceptionHandlerActionMethodHolder implements MethodHolder<Class<?>> {
    private AnnotationHandler annotationHandler_;

    private Map<Class<?>, Method> methodMap_ = new HashMap<Class<?>, Method>();

    public ExceptionHandlerActionMethodHolder(Class<?> handlerClass,
            AnnotationHandler annotationHandler) {
        annotationHandler_ = annotationHandler;

        for (Method method : ClassUtils.getMethods(handlerClass)) {
            ExceptionHandler annotation = annotationHandler_.getAnnotation(
                    method, ExceptionHandler.class);
            if (annotation != null) {
                methodMap_.put(annotation.value(), method);
            }
        }
    }

    public Method getMethod(final Class<?> exceptionClass) {
        Method method = null;
        Class<?> clazz = exceptionClass;
        do {
            method = methodMap_.get(clazz);
        } while (method == null
                && (clazz = clazz.getSuperclass()) != Object.class);
        return method;
    }
}
