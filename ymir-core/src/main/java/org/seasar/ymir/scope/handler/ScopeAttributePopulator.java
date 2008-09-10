package org.seasar.ymir.scope.handler;

/**
 * スコープから値を取り出してページにポピュレートするためのクラスです。
 * 
 * @author YOKOTA Takehiko
 */
public interface ScopeAttributePopulator {
    public void populateTo(Object component, String name);
}
