package org.seasar.ymir.annotation.handler.impl;

import org.seasar.ymir.annotation.SuppressInheritance;
import org.seasar.ymir.constraint.annotation.Required;
import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Ins;
import org.seasar.ymir.scope.impl.RequestParameterScope;
import org.seasar.ymir.scope.impl.SessionScope;

@Required
public class Inherited extends InheritedBase {
    @Required
    @Override
    public void hoe() {
    }

    @SuppressInheritance
    public void hoe2() {
    }
}
