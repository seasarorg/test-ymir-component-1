package org.seasar.ymir.annotation.handler.impl;

import org.seasar.ymir.constraint.annotation.Numeric;
import org.seasar.ymir.constraint.annotation.Required;
import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Ins;
import org.seasar.ymir.scope.impl.RequestParameterScope;
import org.seasar.ymir.scope.impl.SessionScope;

@Numeric
public class InheritedBase extends InheritedBaseBase {
    @In(scopeClass = RequestParameterScope.class)
    public void hoe2() {
    }
}
