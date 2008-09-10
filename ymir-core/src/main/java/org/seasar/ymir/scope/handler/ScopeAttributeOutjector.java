package org.seasar.ymir.scope.handler;

/**
 * ページから値をスコープにアウトジェクトするためのインタフェースです。
 * 
 * @author YOKOTA Takehiko
 */
public interface ScopeAttributeOutjector {
    void outjectFrom(Object component, String actionName);
}
