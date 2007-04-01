package org.seasar.ymir.extension.freemarker;

import org.seasar.ymir.annotation.SuppressConstraints;
import org.seasar.ymir.constraint.annotation.Required;

@Required("test")
public class HoePageBase {

    @Required("get")
    @SuppressConstraints
    public String getHoe() {
        return null;
    }

    @Required("set")
    public void setHoe(String hoe) {
    }

    @Required("method")
    public void _get() {
    }
}
