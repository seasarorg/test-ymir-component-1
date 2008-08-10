package org.seasar.ymir.session.impl;

public interface AttributeListener {
    void notifyGetAttribute(String name);

    void notifySetAttribute(String name);
}
