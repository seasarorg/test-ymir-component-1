package org.seasar.ymir.annotation.handler.impl;

import org.seasar.ymir.constraint.annotation.Numeric;
import org.seasar.ymir.constraint.annotation.Required;

@Numeric
@Required(allowWhitespace = false)
public class InheritedBase {
    @Numeric
    @Required(allowWhitespace = false)
    public void hoe() {
    }
}
