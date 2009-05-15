package org.seasar.ymir.annotation.handler.impl;

import org.seasar.ymir.constraint.annotation.Numeric;
import org.seasar.ymir.constraint.annotation.Required;

@Required(allowWhitespace = false)
public class InheritedBaseBase {
    @Numeric
    @Required(allowWhitespace = false)
    public void hoe() {
    }
}
