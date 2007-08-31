package org.seasar.ymir;

public interface Acceptor<V> {
    Object accept(V visitor);
}
