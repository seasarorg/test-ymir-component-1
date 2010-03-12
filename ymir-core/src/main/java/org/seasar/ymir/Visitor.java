package org.seasar.ymir;

/**
 * Visitorパターンを実装するためのVisitorインタフェースです。
 * 
 * @author YOKOTA Takehiko
 */
public interface Visitor<R, A> {
    /**
     * 指定されたAcceptorについて処理を行います。
     * 
     * @param acceptor Acceptor。
     * @param parameters パラメータ。
     * @return 処理結果。
     */
    R visit(A acceptor, Object... parameters);
}
