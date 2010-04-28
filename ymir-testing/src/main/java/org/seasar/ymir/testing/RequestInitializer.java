package org.seasar.ymir.testing;

/**
 * YmirTestCaseでYmirにリクエストを処理させる際、
 * 処理の開始直前に行なう処理を定義するためのインタフェースです。
 * 
 * @author skirnir
 */
public interface RequestInitializer {
    /**
     * リクエストの任意の初期化処理を行ないます。
     * <p>このメソッドはYmirTestCaseのprocess()メソッドからYmirの処理を呼び出す前に
     * 呼び出されます。
     * </p>
     */
    void initialize();
}
