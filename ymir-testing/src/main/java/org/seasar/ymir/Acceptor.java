package org.seasar.ymir;

/**
 * Visitorパターンを実現するためのAcceptorインタフェースです。
 * 
 * @author YOKOTA Takehiko
 */
public interface Acceptor<V> {
    <R> R accept(V visitor);
}
