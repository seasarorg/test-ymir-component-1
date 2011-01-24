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
     * <p>このメソッドは<code>getMethodInvoker().shouldInvoke()</code>
     * の短縮形です。
     * </p>
     * 
     * @return アクションを実行可能かどうか。
     * @see #invoke()
     */
    boolean shouldInvoke();

    /**
     * アクションを実行します。
     * <p>このメソッドは<code>getMethodInvoker().invoke(getTarget())</code>
     * の短縮形です。
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
     * <p>このメソッドは<code>getMethodInvoker().getReturnType()</code>
     * の短縮形です。
     * </p>
     * 
     * @return アクションの実行結果の型を表すClassオブジェクト。
     */
    Class<? extends Object> getReturnType();
}
