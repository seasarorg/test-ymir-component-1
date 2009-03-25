package org.seasar.ymir.impl;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.util.ArrayUtil;
import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.MethodHolder;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.handler.annotation.ExceptionHandler;
import org.seasar.ymir.util.ClassUtils;

class ExceptionHandlerActionMethodHolder implements
        MethodHolder<ExceptionHandlerActionMethodCondition> {
    private AnnotationHandler annotationHandler_;

    private Map<Class<?>, Entry[]> entriesMap_ = new HashMap<Class<?>, Entry[]>();

    public ExceptionHandlerActionMethodHolder(Class<?> handlerClass,
            boolean global, AnnotationHandler annotationHandler) {
        annotationHandler_ = annotationHandler;

        for (Method method : ClassUtils.getMethods(handlerClass)) {
            ExceptionHandler annotation = annotationHandler_.getAnnotation(
                    method, ExceptionHandler.class);
            if (annotation != null) {
                if (global) {
                    if (annotation.actionName().length > 0) {
                        throw new IllegalClientCodeRuntimeException(
                                "Can't specify 'actionName' element of @ExceptionHandler for a global handler: method="
                                        + method);
                    }
                }

                Class<?> exceptionClass = annotation.value();
                if (global) {
                    if (exceptionClass != Throwable.class) {
                        throw new IllegalClientCodeRuntimeException(
                                "Can't specify 'value' element of @ExceptionHandler for a global handler: method="
                                        + method);
                    }
                } else {
                    if (exceptionClass == Throwable.class) {
                        for (Class<?> type : method.getParameterTypes()) {
                            if (Throwable.class.isAssignableFrom(type)) {
                                exceptionClass = type;
                                break;
                            }
                        }
                    }
                }

                Entry entry = new Entry(method, annotation);
                Entry[] entries = entriesMap_.get(exceptionClass);
                if (entries == null) {
                    entries = new Entry[] { entry };
                } else {
                    if (entry.getAnnotation().actionName().length == 0
                            && entries[entries.length - 1].getAnnotation()
                                    .actionName().length == 0) {
                        throw new IllegalClientCodeRuntimeException(
                                "Duplicate @ExceptionHandler for "
                                        + exceptionClass
                                        + " found in a handler class '"
                                        + ClassUtils
                                                .getPrettyName(handlerClass)
                                        + "'");
                    }

                    entries = (Entry[]) ArrayUtil.add(entries, entry);
                    Arrays.sort(entries);
                }
                entriesMap_.put(exceptionClass, entries);
            }
        }
    }

    public Method getMethod(
            final ExceptionHandlerActionMethodCondition condition) {
        Entry[] entries = null;
        Class<?> clazz = condition.getExceptionClass();
        String actionName = condition.getActionName();
        do {
            entries = entriesMap_.get(clazz);
        } while (entries == null
                && (clazz = clazz.getSuperclass()) != Object.class);
        if (entries != null) {
            for (Entry entry : entries) {
                String[] actionNames = entry.getAnnotation().actionName();
                if (actionNames.length == 0) {
                    return entry.getMethod();
                } else if (actionName != null) {
                    for (String name : actionNames) {
                        if (name.equals(actionName)) {
                            return entry.getMethod();
                        }
                    }
                }
            }
        }
        return null;
    }

    static class Entry implements Comparable<Entry> {
        private Method method_;

        private ExceptionHandler annotation_;

        Entry(Method method, ExceptionHandler annotation) {
            method_ = method;
            annotation_ = annotation;
        }

        public Method getMethod() {
            return method_;
        }

        public ExceptionHandler getAnnotation() {
            return annotation_;
        }

        public int compareTo(Entry o) {
            String[] a1 = getAnnotation().actionName();
            String[] a2 = o.getAnnotation().actionName();
            int cmp = a2.length - a1.length;
            if (cmp == 0) {
                for (int i = 0; i < a1.length && cmp == 0; i++) {
                    cmp = a1[i].compareTo(a2[i]);
                }
            }
            return cmp;
        }
    }
}
