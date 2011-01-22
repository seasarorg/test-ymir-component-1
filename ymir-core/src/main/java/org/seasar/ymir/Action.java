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
     * アクションの実行対象であるオブジェクトのクラスを返します。
     * このメソッドは基本的にAOPによってエンハンスされる前のクラスを返すため、
     * <p><code>getTarget().getClass()</p>の返り値と一致するとは限りません。
     * 
     * @return アクションの実行対象であるオブジェクトのクラス。nullが返ることはありません。
     * @since 1.0.7
     */
    Class<?> getTargetClass();

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
     * <p>通常、アクションの実行とはアクションの実行対象であるオブジェクトについて
     * {@link #getMethodInvoker()}が返すMethodInvokerのメソッドを呼び出すことです。
     * </p>
     * <p>{@link #shouldInvoke()}がfalseである場合、
     * このメソッドを呼び出してはいけません。
     * </p>
     * 
     * @return 実行結果。
     * @throws WrappingRuntimeException メソッド実行に失敗した場合。
     * @see #shouldInvoke()
     */
    Object invoke() throws WrappingRuntimeException;

    /**
     * アクションの実行結果の型を表すClassオブジェクトを返します。
     * 
     * @return アクションの実行結果の型を表すClassオブジェクト。
     */
    Class<? extends Object> getReturnType();
}
