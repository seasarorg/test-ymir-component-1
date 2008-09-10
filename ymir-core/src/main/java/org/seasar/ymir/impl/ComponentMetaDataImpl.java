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
import org.seasar.ymir.ComponentMetaData;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.MethodNotFoundRuntimeException;
import org.seasar.ymir.Phase;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.annotation.In;
import org.seasar.ymir.annotation.Invoke;
import org.seasar.ymir.annotation.Out;
import org.seasar.ymir.annotation.Populate;
import org.seasar.ymir.annotation.Protected;
import org.seasar.ymir.annotation.Resolve;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.scope.handler.ScopeAttributeInjector;
import org.seasar.ymir.scope.handler.ScopeAttributeOutjector;
import org.seasar.ymir.scope.handler.ScopeAttributePopulator;
import org.seasar.ymir.scope.handler.ScopeAttributeResolver;
import org.seasar.ymir.scope.handler.impl.ScopeAttributeInjectorImpl;
import org.seasar.ymir.scope.handler.impl.ScopeAttributeOutjectorImpl;
import org.seasar.ymir.scope.handler.impl.ScopeAttributePopulatorImpl;
import org.seasar.ymir.scope.handler.impl.ScopeAttributeResolverImpl;
import org.seasar.ymir.scope.impl.ComponentScope;
import org.seasar.ymir.scope.impl.RequestScope;
import org.seasar.ymir.util.BeanUtils;
import org.seasar.ymir.util.ClassUtils;

public class ComponentMetaDataImpl implements ComponentMetaData {
    private Class<?> class_;

    private S2Container container_;

    private AnnotationHandler annotationHandler_;

    private ScopeManager scopeManager_;

    private TypeConversionManager typeConversionManager_;

    private Set<String> protectedNameSet_ = new HashSet<String>();

    private List<ScopeAttributeInjector> scopeAttributeInjectorList_ = new ArrayList<ScopeAttributeInjector>();

    private List<ScopeAttributeOutjector> scopeAttributeOutjectorList_ = new ArrayList<ScopeAttributeOutjector>();

    private Map<Phase, Method[]> methodsMap_ = new HashMap<Phase, Method[]>();

    private Map<Scope, ScopeAttributePopulatorImpl> scopeAttributePopulatorMap_ = new HashMap<Scope, ScopeAttributePopulatorImpl>();

    private Map<Method, ScopeAttributeResolver[]> scopeAttributeResolversMap_ = new HashMap<Method, ScopeAttributeResolver[]>();

    public ComponentMetaDataImpl(Class<?> clazz, S2Container container,
            AnnotationHandler annotationHandler, ScopeManager scopeManager,
            TypeConversionManager typeConversionManager) {
        class_ = clazz;
        container_ = container;
        annotationHandler_ = annotationHandler;
        scopeManager_ = scopeManager;
        typeConversionManager_ = typeConversionManager;
        Method[] methods = ClassUtils.getMethods(clazz);
        for (int i = 0; i < methods.length; i++) {
            register(methods[i]);
        }
    }

    // TODO [Ymir1.0.x] 削除する。
    public boolean isProtected(String propertyName) {
        return protectedNameSet_.contains(BeanUtils
                .getFirstSimpleSegment(propertyName));
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
        boolean shouldProtect = false;

        Populate[] populates = annotationHandler_.getAnnotations(method,
                Populate.class);
        if (populates.length > 0) {
            shouldProtect = true;
        }
        for (Populate populate : populates) {
            registerForPopulationFromScope(populate, method);
        }

        In[] ins = annotationHandler_.getAnnotations(method, In.class);
        if (ins.length > 0) {
            shouldProtect = true;
        }
        for (In in : ins) {
            registerForInjectionFromScope(in, method);
        }

        Out[] outs = annotationHandler_.getAnnotations(method, Out.class);
        // パラメータをSetterで受けて@OutつきGetterでオブジェクトスコープにOutjectするケース
        // があるため、@Outがついている場合はプロテクトしない。
        for (Out out : outs) {
            registerForOutjectionToScope(out, method);
        }

        Invoke invoke = annotationHandler_.getAnnotation(method, Invoke.class);
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

        if (annotationHandler_.isAnnotationPresent(method, Binding.class)
                || annotationHandler_.isAnnotationPresent(method,
                        Protected.class)) {
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

        // メソッドの引数についてScopeAttributeResolverを生成して登録する。
        ScopeAttributeResolver[] resolvers = new ScopeAttributeResolver[types.length];
        for (int i = 0; i < types.length; i++) {
            ScopeAttributeResolverImpl resolver = null;
            Resolve[] is = annotationHandler_.getParameterAnnotations(method,
                    i, Resolve.class);
            if (is.length > 0) {
                resolver = new ScopeAttributeResolverImpl(types[i],
                        scopeManager_, typeConversionManager_);
                for (int j = 0; j < is.length; j++) {
                    resolver.addEntry(getScope(is[j]), is[j].value(), is[j]
                            .required());
                }
            }
            resolvers[i] = resolver;
        }
        scopeAttributeResolversMap_.put(method, resolvers);
    }

    void registerForPopulationFromScope(Populate populate, Method method) {
        Scope scope = getScope(populate);

        ScopeAttributePopulatorImpl populator = scopeAttributePopulatorMap_
                .get(scope);
        if (populator == null) {
            populator = new ScopeAttributePopulatorImpl(scope, scopeManager_,
                    typeConversionManager_);
            scopeAttributePopulatorMap_.put(scope, populator);
        }

        if (populate.name().length() == 0) {
            populator.addEntry(method, populate.actionName());
        } else {
            populator.addEntry(populate.name(), method, populate.actionName());
        }
    }

    Scope getScope(Populate populate) {
        Object key;
        if (populate.scopeName().length() > 0) {
            key = populate.scopeName();
        } else if (populate.scopeClass() != Scope.class) {
            key = populate.scopeClass();
        } else if (populate.value() != Scope.class) {
            key = populate.value();
        } else {
            key = RequestScope.class;
        }
        return (Scope) getComponent(key);
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

        scopeAttributeInjectorList_.add(new ScopeAttributeInjectorImpl(
                toAttributeName(method.getName(), in.name()), method
                        .getParameterTypes()[0], getScope(in), method, in
                        .injectWhereNull(), in.required(), in.actionName(),
                scopeManager_));
    }

    Scope getScope(In in) {
        Object key;
        if (in.scopeName().length() > 0) {
            key = in.scopeName();
        } else if (in.scopeClass() != Scope.class) {
            key = in.scopeClass();
        } else if (in.value() != Scope.class) {
            key = in.value();
        } else {
            key = RequestScope.class;
        }
        return (Scope) getComponent(key);
    }

    Scope getScope(Resolve resolve) {
        Object key;
        if (resolve.scopeName().length() > 0) {
            key = resolve.scopeName();
        } else if (resolve.scopeClass() != Scope.class) {
            key = resolve.scopeClass();
        } else {
            key = ComponentScope.class;
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

        scopeAttributeOutjectorList_.add(new ScopeAttributeOutjectorImpl(
                toAttributeName(method.getName(), out.name()), getScope(out),
                method, out.outjectWhereNull(), out.actionName()));
    }

    Scope getScope(Out out) {
        Object key;
        if (out.scopeName().length() > 0) {
            key = out.scopeName();
        } else if (out.scopeClass() != Scope.class) {
            key = out.scopeClass();
        } else if (out.value() != Scope.class) {
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
