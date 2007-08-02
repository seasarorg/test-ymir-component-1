package org.seasar.ymir;

public interface Visitor<A> {
    Object visit(A acceptor);
}
