package org.seasar.ymir.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.Globals;
import org.seasar.ymir.PageMetaData;
import org.seasar.ymir.Phase;
import org.seasar.ymir.ScopeAttribute;
import org.seasar.ymir.annotation.In;
import org.seasar.ymir.annotation.Invoke;
import org.seasar.ymir.annotation.Out;
import org.seasar.ymir.annotation.Protected;
import org.seasar.ymir.annotation.RequestParameter;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.scope.impl.RequestScope;
import org.seasar.ymir.util.BeanUtils;

public class PageMetaDataImpl implements PageMetaData {
    private Class<?> class_;

    private S2Container container_;

    private Set<String> protectedNameSet_ = new HashSet<String>();

    private Set<String> permittedNameSet_ = new HashSet<String>();

    private List<ScopeAttribute> injectedScopeAttributeList_ = new ArrayList<ScopeAttribute>();

    private List<ScopeAttribute> outjectedScopeAttributeList_ = new ArrayList<ScopeAttribute>();

    private Map<Phase, Method[]> methodsMap_ = new HashMap<Phase, Method[]>();

    private boolean strictInjection_;

    public PageMetaDataImpl(Class<?> clazz, S2Container container) {
        class_ = clazz;
        container_ = container;
        strictInjection_ = isStrictInjection(container);
        Method[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            register(methods[i]);
        }
    }

    boolean isStrictInjection(S2Container container) {
        return PropertyUtils.valueOf(((ApplicationManager) container
                .getComponent(ApplicationManager.class))
                .findContextApplication().getProperty(
                        Globals.APPKEY_CORE_REQUESTPARAMETER_STRICTINJECTION),
                false);
    }

    public boolean isProtected(String propertyName) {
        if (strictInjection_) {
            return !permittedNameSet_.contains(BeanUtils
                    .getFirstSimpleSegment(propertyName));
        } else {
            return protectedNameSet_.contains(BeanUtils
                    .getFirstSimpleSegment(propertyName));
        }
    }

    public ScopeAttribute[] getInjectedScopeAttributes() {
        return injectedScopeAttributeList_.toArray(new ScopeAttribute[0]);
    }

    public ScopeAttribute[] getOutjectedScopeAttributes() {
        return outjectedScopeAttributeList_.toArray(new ScopeAttribute[0]);
    }

    void register(Method method) {
        boolean shouldProtect = false;

        In in = method.getAnnotation(In.class);
        if (in != null) {
            registerForInjectionFromScope(in, method);
            shouldProtect = true;
        }

        Out out = method.getAnnotation(Out.class);
        if (out != null) {
            registerForOutjectionToScope(out, method);
            // パラメータをSetterで受けて@OutつきGetterでオブジェクトスコープにOutjectするケース
            // があるため、@Outがついている場合はプロテクトしない。
        }

        Invoke invoke = method.getAnnotation(Invoke.class);
        if (invoke != null) {
            if (method.getParameterTypes().length > 0) {
                throw new RuntimeException(
                        "Can't annotate method that has parameter with @Invoke");
            }
            Phase phase = invoke.value();
            Method[] methods = methodsMap_.get(phase);
            if (methods == null) {
                methods = new Method[] { method };
            } else {
                methods = (Method[]) ArrayUtil.add(methods, method);
            }
            methodsMap_.put(phase, methods);
        }

        if (method.isAnnotationPresent(Binding.class)
                || method.isAnnotationPresent(Protected.class)) {
            shouldProtect = true;
        }

        Class<?>[] types = method.getParameterTypes();
        if (method.getName().startsWith("set")
                && types.length == 1
                && (!FormFile.class.isAssignableFrom(types[0])
                        && types[0].isInterface() || types[0].isArray()
                        && !FormFile.class.isAssignableFrom(types[0]
                                .getComponentType())
                        && types[0].getComponentType().isInterface())) {
            // S2Container用のsetterとみなしてプロテクトする。
            shouldProtect = true;
        }

        if (shouldProtect) {
            protectedNameSet_.add(BeanUtils.toPropertyName(method.getName(),
                    false));
        }

        if (strictInjection_
                && method.isAnnotationPresent(RequestParameter.class)) {
            permittedNameSet_.add(BeanUtils.toPropertyName(method.getName(),
                    false));
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

        injectedScopeAttributeList_.add(new ScopeAttribute(container_,
                toAttributeName(method.getName(), in.name()), getScope(in),
                method, in.injectWhereNull(), null, false, in.actionName()));
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

        outjectedScopeAttributeList_.add(new ScopeAttribute(container_,
                toAttributeName(method.getName(), out.name()), getScope(out),
                null, false, method, out.outjectWhereNull(), out.actionName()));
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
            return BeanUtils.toPropertyName(methodName, false);
        }
    }

    Object getComponent(Object key) {
        return container_.getComponent(key);
    }

    public Method[] getMethods(Phase phase) {
        return methodsMap_.get(phase);
    }
}
