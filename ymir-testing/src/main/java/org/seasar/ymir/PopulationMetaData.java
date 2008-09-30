package org.seasar.ymir;

import org.seasar.ymir.scope.Scope;

public interface PopulationMetaData {
    Scope getScope();

    boolean isProtected(String name, String actionName);
}
