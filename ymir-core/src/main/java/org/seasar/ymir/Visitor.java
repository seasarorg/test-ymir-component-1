package org.seasar.ymir;

/**
 * Visitorパターンを実装するためのVisitorインタフェースです。
 * 
 * @author YOKOTA Takehiko
 */
public interface Visitor<A> {
    /**
     * 指定されたAcceptorについて処理を行います。
     * 
     * @param acceptor Acceptor。
     * @return 処理結果。
     */
    Object visit(A acceptor);
}
