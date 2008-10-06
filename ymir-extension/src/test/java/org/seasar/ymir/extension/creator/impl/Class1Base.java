package org.seasar.ymir.extension.creator.impl;

import org.seasar.ymir.annotation.Meta;

public class Class1Base {

    @Meta(name="source", value="return \"return value\";")
    public String _get() {

        return "return value";
    }
}
