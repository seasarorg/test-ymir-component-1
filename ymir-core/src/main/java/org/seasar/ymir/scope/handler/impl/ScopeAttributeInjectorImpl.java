package org.seasar.ymir.scope.handler.impl;

import java.lang.reflect.Method;

import org.seasar.framework.log.Logger;
import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.ymir.scope.AttributeNotFoundRuntimeException;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.scope.handler.ScopeAttributeInjector;
import org.seasar.ymir.util.ClassUtils;

public class ScopeAttributeInjectorImpl extends AbstractScopeAttributeHandler
        implements ScopeAttributeInjector {
    private static final Logger logger_ = Logger
            .getLogger(ScopeAttributeInjectorImpl.class);

    private Class<?> type_;

    private Class<?> componentType_;

    private boolean required_;

    private ScopeManager scopeManager_;

    public ScopeAttributeInjectorImpl(String name, Class<?> type, Scope scope,
            Method injectionMethod, boolean injectWhereNull, boolean required,
            String[] enabledActionNames, ScopeManager scopeManager) {
        super(name, scope, injectionMethod, injectWhereNull, enabledActionNames);
        type_ = type;
        componentType_ = ClassUtils.toComponentType(type);
        required_ = required;
        scopeManager_ = scopeManager;
    }

    public void injectTo(Object component, String actionName)
            throws AttributeNotFoundRuntimeException {
        if (!isEnabled(actionName)) {
            return;
        }

        Object value;
        try {
            value = scopeManager_.getAttribute(scope_, name_, type_, required_,
                    invokeWhereNull_);
        } catch (AttributeNotFoundRuntimeException ex) {
            throw ex.setMethod(method_).setComponent(component);
        }

        if (value != null || invokeWhereNull_) {
            boolean removeValue = false;
            try {
                method_.invoke(component, new Object[] { value });
            } catch (Throwable t) {
                // Exceptionをスローしつつ値を消すようにする。
                removeValue = true;
                throw new IORuntimeException(
                        "Can't inject scope attribute: scope=" + scope_
                                + ", attribute name=" + name_ + ", value="
                                + value + ", write method=" + method_, t);
            } finally {
                if (removeValue) {
                    scope_.setAttribute(name_, null);
                }
            }
        }
    }
}
