package org.seasar.ymir.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.ymir.ScopeAttribute;
import org.seasar.ymir.annotation.In;
import org.seasar.ymir.annotation.Out;
import org.seasar.ymir.annotation.Protected;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.scope.impl.RequestScope;
import org.seasar.ymir.util.BeanUtils;

public class PagePropertyBag {
    private Class<?> class_;

    private S2Container container_;

    private Set<String> protectedSetterNameSet_ = new HashSet<String>();

    private List<ScopeAttribute> injectedScopeAttributeList_ = new ArrayList<ScopeAttribute>();

    private List<ScopeAttribute> outjectedScopeAttributeList_ = new ArrayList<ScopeAttribute>();

    public PagePropertyBag(Class<?> clazz, S2Container container) {
        class_ = clazz;
        container_ = container;
        Method[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            register(methods[i]);
        }
    }

    public boolean isProtected(String propertyName) {
        return protectedSetterNameSet_.contains(propertyName);
    }

    public ScopeAttribute[] getInjectedScopeAttributes() {
        return injectedScopeAttributeList_.toArray(new ScopeAttribute[0]);
    }

    public ScopeAttribute[] getOutjectedScopeAttributes() {
        return outjectedScopeAttributeList_.toArray(new ScopeAttribute[0]);
    }

    void register(Method method) {
        In in = method.getAnnotation(In.class);
        if (in != null) {
            registerForInjectionFromScope(in, method);
            protectedSetterNameSet_.add(BeanUtils.toPropertyName(method
                    .getName()));
            return;
        }

        Out out = method.getAnnotation(Out.class);
        if (method.isAnnotationPresent(Out.class)) {
            registerForOutjectionToScope(out, method);
            return;
        }

        if (method.isAnnotationPresent(Binding.class)
                || method.isAnnotationPresent(Protected.class)) {
            protectedSetterNameSet_.add(BeanUtils.toPropertyName(method
                    .getName()));
            return;
        }

        Class<?>[] types = method.getParameterTypes();
        if (method.getName().startsWith("set")
                && types.length == 1
                && (types[0].isInterface() || types[0].isArray()
                        && types[0].getComponentType().isInterface())) {
            // S2Container用のsetterとみなしてプロテクトする。
            protectedSetterNameSet_.add(BeanUtils.toPropertyName(method
                    .getName()));
            return;
        }
    }

    void registerForInjectionFromScope(In in, Method method) {
        int modifiers = method.getModifiers();
        if (Modifier.isStatic(modifiers)) {
            throw new RuntimeException(
                    "Logic error: @In can't annotate static method: class="
                            + class_.getName() + ", method=" + method);
        } else if (!Modifier.isPublic(modifiers)) {
            throw new RuntimeException(
                    "Logic error: @In can annotate only public method: class="
                            + class_.getName() + ", method=" + method);
        } else if (method.getParameterTypes().length != 1) {
            throw new RuntimeException(
                    "Logic error: @In can't annotate this method: class="
                            + class_.getName() + ", method=" + method);
        }

        injectedScopeAttributeList_.add(new ScopeAttribute(toAttributeName(
                method.getName(), in.name()), getScope(in), method, null));
    }

    Scope getScope(In in) {
        Object key;
        if (in.scopeName().length() > 0) {
            key = in.scopeName();
        } else if (in.scopeClass() != Object.class) {
            key = in.scopeClass();
        } else if (in.value() != Object.class) {
            key = in.value();
        } else {
            key = RequestScope.class;
        }
        return (Scope) getComponent(key);
    }

    void registerForOutjectionToScope(Out out, Method method) {
        int modifiers = method.getModifiers();
        if (Modifier.isStatic(modifiers)) {
            throw new RuntimeException(
                    "Logic error: @Out can't annotate static method: class="
                            + class_.getName() + ", method=" + method);
        } else if (!Modifier.isPublic(modifiers)) {
            throw new RuntimeException(
                    "Logic error: @Out can annotate only public method: class="
                            + class_.getName() + ", method=" + method);
        } else if (method.getParameterTypes().length != 0) {
            throw new RuntimeException(
                    "Logic error: @Out can't annotate this method: class="
                            + class_.getName() + ", method=" + method);
        }

        outjectedScopeAttributeList_.add(new ScopeAttribute(toAttributeName(
                method.getName(), out.name()), getScope(out), null, method));
    }

    Scope getScope(Out out) {
        Object key;
        if (out.scopeName().length() > 0) {
            key = out.scopeName();
        } else if (out.scopeClass() != Object.class) {
            key = out.scopeClass();
        } else if (out.value() != Object.class) {
            key = out.value();
        } else {
            key = RequestScope.class;
        }
        return (Scope) getComponent(key);
    }

    String toAttributeName(String methodName, String explicitName) {
        if (explicitName != null && explicitName.length() > 0) {
            return explicitName;
        } else {
            return BeanUtils.toPropertyName(methodName);
        }
    }

    Object getComponent(Object key) {
        if (container_ != null) {
            return container_.getComponent(key);
        } else {
            return null;
        }
    }
}
