package org.seasar.ymir.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.framework.util.ArrayUtil;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.ComponentMetaData;
import org.seasar.ymir.Phase;
import org.seasar.ymir.annotation.Invoke;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.util.ClassUtils;

public class ComponentMetaDataImpl implements ComponentMetaData {
    private static final Method[] EMPTY_METHODS = new Method[0];

    private ActionManager actionManager_;

    private AnnotationHandler annotationHandler_;

    private Map<Phase, MethodData[]> methodDatasMap_ = new HashMap<Phase, MethodData[]>();

    public ComponentMetaDataImpl(Class<?> clazz, ActionManager actionManager,
            AnnotationHandler annotationHandler) {
        actionManager_ = actionManager;
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
            String[] actionNamePatterns = invoke.actionName();
            MethodData[] methodDatas = methodDatasMap_.get(phase);
            MethodData methodData = new MethodData(method, actionNamePatterns);
            if (methodDatas == null) {
                methodDatas = new MethodData[] { methodData };
            } else {
                methodDatas = (MethodData[]) ArrayUtil.add(methodDatas,
                        methodData);
            }
            methodDatasMap_.put(phase, methodDatas);
        }
    }

    public Method[] getMethods(Phase phase, String actionName) {
        MethodData[] methodDatas = methodDatasMap_.get(phase);
        if (methodDatas == null) {
            return EMPTY_METHODS;
        }

        List<Method> methods = new ArrayList<Method>();
        for (MethodData methodData : methodDatas) {
            if (methodData.isMatched(actionName)) {
                methods.add(methodData.getMethod());
            }
        }
        return methods.toArray(EMPTY_METHODS);
    }

    class MethodData {
        private Method method_;

        private String[] actionNamePatterns_;

        public MethodData(Method method, String[] actionNamePatterns) {
            method_ = method;
            actionNamePatterns_ = actionNamePatterns;
        }

        public Method getMethod() {
            return method_;
        }

        public boolean isMatched(String actionName) {
            return actionManager_.isMatched(actionName, actionNamePatterns_);
        }
    }
}
