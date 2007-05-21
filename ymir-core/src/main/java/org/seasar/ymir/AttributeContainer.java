package org.seasar.ymir;

import java.util.Enumeration;

public interface AttributeContainer {

    Object getAttribute(String name);

    Enumeration<String> getAttributeNames();

    void setAttribute(String name, Object value);

    void removeAttribute(String name);
}
