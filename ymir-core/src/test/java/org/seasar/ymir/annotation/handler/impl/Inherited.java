package org.seasar.ymir.annotation.handler.impl;

import org.seasar.ymir.constraint.annotation.Required;

@Required
public class Inherited extends InheritedBase {
    @Required
    @Override
    public void hoe() {
    }
}
