package org.seasar.ymir.scope.handler;

import org.seasar.ymir.scope.AttributeNotFoundRuntimeException;

/**
 * スコープから値を取り出してページにインジェクトするためのインタフェースです。
 * 
 * @author YOKOTA Takehiko
 */
public interface ScopeAttributeInjector {
    void injectTo(Object component, String actionName)
            throws AttributeNotFoundRuntimeException;
}
