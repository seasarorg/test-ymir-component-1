package org.seasar.ymir.scope.handler.impl;

import java.lang.reflect.Method;

import org.seasar.framework.log.Logger;
import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.scope.Scope;

/**
 * ページから値をスコープにアウトジェクトするためのクラスです。
 * 
 * @author YOKOTA Takehiko
 */
public class ScopeAttributeOutjector extends AbstractScopeAttributeHandler {
    private static final Logger logger_ = Logger
            .getLogger(ScopeAttributeOutjector.class);

    public ScopeAttributeOutjector(String name, Scope scope,
            Method outjectionMethod, boolean outjectWhereNull,
            String[] enabledActionNames, HotdeployManager hotdeployManager,
            TypeConversionManager typeConversionManager) {
        super(name, scope, outjectionMethod, outjectWhereNull,
                enabledActionNames, hotdeployManager, typeConversionManager);
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
            scope_.setAttribute(name_, value);
        }
    }

    public void injectTo(Object component, String actionName) {
        throw new UnsupportedOperationException();
    }
}
