package org.seasar.ymir.scope.handler.impl;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.scope.handler.ScopeAttributeOutjector;
import org.seasar.ymir.util.ClassUtils;

public class ScopeAttributeOutjectorImpl extends AbstractScopeAttributeHandler
        implements ScopeAttributeOutjector {
    private static final Log log_ = LogFactory
            .getLog(ScopeAttributeOutjectorImpl.class);

    public ScopeAttributeOutjectorImpl(String name, Scope scope,
            Method outjectionMethod, boolean outjectWhereNull,
            String[] enabledActionNames) {
        super(name, scope, outjectionMethod, outjectWhereNull,
                enabledActionNames);
    }

    public void outjectFrom(Object component, String actionName) {
        if (!isEnabled(actionName)) {
            return;
        }

        Object value;
        try {
            value = method_.invoke(component, new Object[0]);
        } catch (Throwable t) {
            throw new IORuntimeException(
                    "Can't outject scope attribute: scope=" + scope_
                            + ", attribute name=" + name_ + ", read method="
                            + method_, t);
        }
        if (value != null || invokeWhereNull_) {
            if (log_.isDebugEnabled()) {
                log_.debug(ClassUtils.getPrettyName(component) + " -> "
                        + ClassUtils.getPrettyName(scope_) + ": property="
                        + name_ + ", value=" + value);
            }
            scope_.setAttribute(name_, value);
        }
    }
}
