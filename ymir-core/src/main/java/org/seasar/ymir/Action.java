package org.seasar.ymir;

/**
 * リクエストによって駆動されるアクションを表すインタフェースです。
 * 
 * @author YOKOTA Takehiko
 */
public interface Action {
    /**
     * アクションに対応するメソッドを実行するためのMethodInvokerを返します。
     * 
     * @return MethodInvoker。nullが返ることはありません。
     */
    MethodInvoker getMethodInvoker();

    /**
     * アクションの実行対象であるオブジェクトを返します。
     * 
     * @return アクションの実行対象であるオブジェクト。nullが返ることはありません。
     */
    Object getTarget();

    /**
     * アクションの名前を返します。
     * 
     * @return アクションの名前。nullが返ることはありません。
     */
    String getName();

    /**
     * アクションを実行可能かどうかを返します。
     * 
     * @return アクションを実行可能かどうか。
     * @see #invoke()
     */
    boolean shouldInvoke();

    /**
     * アクションを実行します。
     * <p>アクションの実行とは、通常アクションの実行対象であるオブジェクトについて
     * {@link #getMethodInvoker()}が返すMethodInvokerのメソッドを呼び出すことです。
     * </p>
     * <p>{@link #shouldInvoke()}がfalseである場合、
     * このメソッドを呼び出してはいけません。
     * </p>
     * 
     * @return 実行結果。
     * @see #shouldInvoke()
     */
    Object invoke();
}
