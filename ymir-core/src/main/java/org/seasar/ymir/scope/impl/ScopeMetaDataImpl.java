package org.seasar.ymir.scope.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.MethodNotFoundRuntimeException;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.converter.annotation.TypeConversionHint;
import org.seasar.ymir.scope.Globals;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.scope.ScopeMetaData;
import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Out;
import org.seasar.ymir.scope.annotation.Populate;
import org.seasar.ymir.scope.handler.ScopeAttributeInjector;
import org.seasar.ymir.scope.handler.ScopeAttributeOutjector;
import org.seasar.ymir.scope.handler.ScopeAttributePopulator;
import org.seasar.ymir.scope.handler.ScopeAttributeResolver;
import org.seasar.ymir.scope.handler.impl.ScopeAttributeInjectorImpl;
import org.seasar.ymir.scope.handler.impl.ScopeAttributeOutjectorImpl;
import org.seasar.ymir.scope.handler.impl.ScopeAttributePopulatorImpl;
import org.seasar.ymir.scope.handler.impl.ScopeAttributeResolverImpl;
import org.seasar.ymir.util.BeanUtils;
import org.seasar.ymir.util.ClassUtils;

public class ScopeMetaDataImpl implements ScopeMetaData {
    private static final String SUFFIX_SCOPE = "Scope";

    private Class<?> class_;

    private S2Container container_;

    private ActionManager actionManager_;

    private AnnotationHandler annotationHandler_;

    private ApplicationManager applicationManager_;

    private ScopeManager scopeManager_;

    private TypeConversionManager typeConversionManager_;

    private List<ScopeAttributeInjector> scopeAttributeInjectorList_ = new ArrayList<ScopeAttributeInjector>();

    private List<ScopeAttributeOutjector> scopeAttributeOutjectorList_ = new ArrayList<ScopeAttributeOutjector>();

    private Map<Scope, ScopeAttributePopulatorImpl> scopeAttributePopulatorMap_ = new HashMap<Scope, ScopeAttributePopulatorImpl>();

    private Map<Method, ScopeAttributeResolver[]> scopeAttributeResolversMap_ = new HashMap<Method, ScopeAttributeResolver[]>();

    public ScopeMetaDataImpl(Class<?> clazz, S2Container container,
            ActionManager actionManager, AnnotationHandler annotationHandler,
            ApplicationManager applicationManager, ScopeManager scopeManager,
            TypeConversionManager typeConversionManager) {
        class_ = clazz;
        container_ = container;
        actionManager_ = actionManager;
        annotationHandler_ = annotationHandler;
        applicationManager_ = applicationManager;
        scopeManager_ = scopeManager;
        typeConversionManager_ = typeConversionManager;
        Method[] methods = ClassUtils.getMethods(clazz);
        for (int i = 0; i < methods.length; i++) {
            register(methods[i]);
        }
    }

    public ScopeAttributePopulator[] getScopeAttributePopulators() {
        return scopeAttributePopulatorMap_.values().toArray(
                new ScopeAttributePopulator[0]);
    }

    public ScopeAttributeInjector[] getScopeAttributeInjectors() {
        return scopeAttributeInjectorList_
                .toArray(new ScopeAttributeInjector[0]);
    }

    public ScopeAttributeOutjector[] getScopeAttributeOutjectors() {
        return scopeAttributeOutjectorList_
                .toArray(new ScopeAttributeOutjector[0]);
    }

    void register(Method method) {
        for (Populate populate : annotationHandler_.getAnnotations(method,
                Populate.class)) {
            registerForPopulationFromScope(populate, method);
        }

        for (In in : annotationHandler_.getAnnotations(method, In.class)) {
            registerForInjectionFromScope(in, method);
        }

        for (Out out : annotationHandler_.getAnnotations(method, Out.class)) {
            registerForOutjectionToScope(out, method);
        }

        // メソッドの引数についてScopeAttributeResolverを生成して登録する。
        Class<?>[] types = method.getParameterTypes();
        ScopeAttributeResolver[] resolvers = new ScopeAttributeResolver[types.length];
        for (int i = 0; i < types.length; i++) {
            ScopeAttributeResolverImpl resolver = null;
            In[] is = annotationHandler_.getParameterAnnotations(method, i,
                    In.class);
            Populate[] ps = annotationHandler_.getParameterAnnotations(method,
                    i, Populate.class);
            if (is.length + ps.length > 0) {
                resolver = new ScopeAttributeResolverImpl(types[i],
                        annotationHandler_.getMarkedParameterAnnotations(
                                method, i, TypeConversionHint.class),
                        scopeManager_, typeConversionManager_);
                for (In in : is) {
                    resolver.addEntry(getScope(in), in.name(), in.required());
                }
                for (Populate populate : ps) {
                    resolver.addEntry(getScope(populate), populate.name(),
                            false);
                }
            }
            resolvers[i] = resolver;
        }
        scopeAttributeResolversMap_.put(method, resolvers);
    }

    void registerForPopulationFromScope(Populate populate, Method method) {
        int modifiers = method.getModifiers();
        if (Modifier.isStatic(modifiers)) {
            throw new IllegalClientCodeRuntimeException(
                    "Logic error: @Populate can't annotate static method: class="
                            + class_.getName() + ", method=" + method);
        } else if (!Modifier.isPublic(modifiers)) {
            throw new IllegalClientCodeRuntimeException(
                    "Logic error: @Populate can annotate only public method: class="
                            + class_.getName() + ", method=" + method);
        }

        Scope scope = getScope(populate);

        ScopeAttributePopulatorImpl populator = scopeAttributePopulatorMap_
                .get(scope);
        if (populator == null) {
            populator = new ScopeAttributePopulatorImpl(scope, actionManager_,
                    annotationHandler_, scopeManager_, typeConversionManager_);
            scopeAttributePopulatorMap_.put(scope, populator);
        }

        if (populate.name().length() == 0) {
            populator.addEntry(method, populate.populateWhereNull(), populate
                    .actionName());
        } else {
            populator.addEntry(populate.name(), method, populate
                    .populateWhereNull(), populate.actionName());
        }
    }

    void registerForInjectionFromScope(In in, Method method) {
        int modifiers = method.getModifiers();
        if (Modifier.isStatic(modifiers)) {
            throw new IllegalClientCodeRuntimeException(
                    "Logic error: @In can't annotate static method. @In usually annotates non-static setter method: class="
                            + class_.getName() + ", method=" + method);
        } else if (!Modifier.isPublic(modifiers)) {
            throw new IllegalClientCodeRuntimeException(
                    "Logic error: @In can annotate only public method. @In usually annotates public setter method: class="
                            + class_.getName() + ", method=" + method);
        } else if (method.getParameterTypes().length != 1) {
            throw new IllegalClientCodeRuntimeException(
                    "Logic error: @In can't annotate this method. @In usually annotates setter method: class="
                            + class_.getName() + ", method=" + method);
        }

        scopeAttributeInjectorList_.add(new ScopeAttributeInjectorImpl(
                toAttributeName(method.getName(), in.name()), method
                        .getParameterTypes()[0],
                annotationHandler_.getMarkedAnnotations(method,
                        TypeConversionHint.class), getScope(in), method, in
                        .injectWhereNull(), in.required(), in.actionName(),
                actionManager_, scopeManager_));
    }

    Scope getScope(In in) {
        Object key;
        if (in.scopeName().length() > 0) {
            key = normalizeScopeName(in.scopeName());
        } else if (in.scopeClass() != Scope.class) {
            key = in.scopeClass();
        } else if (in.value() != Scope.class) {
            key = in.value();
        } else {
            key = normalizeScopeName(getDefaultScopeName());
        }
        return (Scope) getComponent(key);
    }

    Scope getScope(Populate populate) {
        Object key;
        if (populate.scopeName().length() > 0) {
            key = normalizeScopeName(populate.scopeName());
        } else if (populate.scopeClass() != Scope.class) {
            key = populate.scopeClass();
        } else if (populate.value() != Scope.class) {
            key = populate.value();
        } else {
            key = normalizeScopeName(getDefaultScopeName());
        }
        return (Scope) getComponent(key);
    }

    Scope getScope(Out out) {
        Object key;
        if (out.scopeName().length() > 0) {
            key = normalizeScopeName(out.scopeName());
        } else if (out.scopeClass() != Scope.class) {
            key = out.scopeClass();
        } else if (out.value() != Scope.class) {
            key = out.value();
        } else {
            key = normalizeScopeName(getDefaultScopeName());
        }
        return (Scope) getComponent(key);
    }

    private String getDefaultScopeName() {
        return applicationManager_.findContextApplication().getProperty(
                Globals.APPKEY_CORE_SCOPE_DEFAULTSCOPENAME,
                Globals.DEFAULT_CORE_SCOPE_DEFAULTSCOPENAME);
    }

    String normalizeScopeName(String scopeName) {
        if (scopeName != null && scopeName.length() > 0
                && !scopeName.endsWith(SUFFIX_SCOPE)) {
            return scopeName + SUFFIX_SCOPE;
        } else {
            return scopeName;
        }
    }

    void registerForOutjectionToScope(Out out, Method method) {
        int modifiers = method.getModifiers();
        if (Modifier.isStatic(modifiers)) {
            throw new IllegalClientCodeRuntimeException(
                    "Logic error: @Out can't annotate static method. @Out usually annotates non-static getter method: class="
                            + class_.getName() + ", method=" + method);
        } else if (!Modifier.isPublic(modifiers)) {
            throw new IllegalClientCodeRuntimeException(
                    "Logic error: @Out can annotate only public method. @Out usually annotates public getter method: class="
                            + class_.getName() + ", method=" + method);
        } else if (method.getParameterTypes().length != 0
                || method.getReturnType() == Void.TYPE) {
            throw new IllegalClientCodeRuntimeException(
                    "Logic error: @Out can't annotate this method. @Out usually annotates getter method: class="
                            + class_.getName() + ", method=" + method);
        }

        scopeAttributeOutjectorList_.add(new ScopeAttributeOutjectorImpl(
                toAttributeName(method.getName(), out.name()), getScope(out),
                method, out.outjectWhereNull(), out.actionName(),
                actionManager_));
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

    public ScopeAttributeResolver[] getScopeAttributeResolversForParameters(
            Method method) throws MethodNotFoundRuntimeException {
        ScopeAttributeResolver[] resolvers = scopeAttributeResolversMap_
                .get(method);
        if (resolvers != null) {
            return resolvers;
        } else {
            throw new MethodNotFoundRuntimeException().setMethod(method);
        }
    }
}
