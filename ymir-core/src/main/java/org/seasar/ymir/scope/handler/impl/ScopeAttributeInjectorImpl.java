package org.seasar.ymir.scope.handler.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.scope.AttributeNotFoundRuntimeException;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.scope.handler.ScopeAttributeInjector;
import org.seasar.ymir.util.ClassUtils;

public class ScopeAttributeInjectorImpl extends AbstractScopeAttributeHandler
        implements ScopeAttributeInjector {
    private Class<?> type_;

    private Annotation[] hint_;

    private boolean required_;

    private ScopeManager scopeManager_;

    private static final Log log_ = LogFactory
            .getLog(ScopeAttributeInjectorImpl.class);

    public ScopeAttributeInjectorImpl(String name, Class<?> type,
            Annotation[] hint, Scope scope, Method injectionMethod,
            boolean injectWhereNull, boolean required,
            String[] enabledActionNames, ActionManager actionManager,
            ScopeManager scopeManager) {
        super(name, scope, injectionMethod, injectWhereNull,
                enabledActionNames, actionManager);
        type_ = type;
        hint_ = hint;
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
            value = scopeManager_.getAttribute(scope_, name_, type_, hint_,
                    required_, invokeWhereNull_);
        } catch (AttributeNotFoundRuntimeException ex) {
            throw ex.setMethod(method_).setComponent(component);
        }

        if (value != null || invokeWhereNull_) {
            if (log_.isDebugEnabled()) {
                log_.debug(ClassUtils.getPrettyName(scope_) + " -> "
                        + ClassUtils.getPrettyName(component) + ": property="
                        + name_ + ", value=" + value);
            }
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
