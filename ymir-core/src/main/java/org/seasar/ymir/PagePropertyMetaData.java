package org.seasar.ymir;

public interface PagePropertyMetaData {
    boolean isProtected(String propertyName);

    ScopeAttribute[] getInjectedScopeAttributes();

    ScopeAttribute[] getOutjectedScopeAttributes();
}
