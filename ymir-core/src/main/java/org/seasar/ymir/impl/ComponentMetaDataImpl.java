package org.seasar.ymir.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.util.ArrayUtil;
import org.seasar.ymir.ComponentMetaData;
import org.seasar.ymir.Phase;
import org.seasar.ymir.annotation.Invoke;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.util.ClassUtils;

public class ComponentMetaDataImpl implements ComponentMetaData {
    private AnnotationHandler annotationHandler_;

    private Map<Phase, Method[]> methodsMap_ = new HashMap<Phase, Method[]>();

    public ComponentMetaDataImpl(Class<?> clazz,
            AnnotationHandler annotationHandler) {
        annotationHandler_ = annotationHandler;
        Method[] methods = ClassUtils.getMethods(clazz);
        for (int i = 0; i < methods.length; i++) {
            register(methods[i]);
        }
    }

    void register(Method method) {
        Invoke invoke = annotationHandler_.getAnnotation(method, Invoke.class);
        if (invoke != null) {
            Phase phase = invoke.value();
            Method[] methods = methodsMap_.get(phase);
            if (methods == null) {
                methods = new Method[] { method };
            } else {
                methods = (Method[]) ArrayUtil.add(methods, method);
            }
            methodsMap_.put(phase, methods);
        }
    }

    public Method[] getMethods(Phase phase) {
        return methodsMap_.get(phase);
    }
}
