package org.seasar.ymir.impl;

import org.seasar.ymir.constraint.annotation.Fuga;
import org.seasar.ymir.constraint.annotation.Fugas;

public class Hoe7 {
    @Fugas( { @Fuga("1"), @Fuga("2") })
    @Fuga("3")
    public void _get() {
    }
}
