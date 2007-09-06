package org.seasar.ymir;

import java.lang.reflect.Method;

public interface PageMetaData {
    boolean isProtected(String propertyName);

    ScopeAttribute[] getInjectedScopeAttributes();

    ScopeAttribute[] getOutjectedScopeAttributes();

    Method[] getMethods(Phase phase);
}
