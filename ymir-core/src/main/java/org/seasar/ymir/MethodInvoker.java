package org.seasar.ymir;

import java.lang.reflect.Method;

/**
 * オブジェクトのメソッドを実行するためのクラスです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要はありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface MethodInvoker {
    /**
     * 実行するメソッドを表すMethodオブジェクトを返します。
     * @return Methodオブジェクト。
     */
    Method getMethod();

    /**
     * メソッド実行時に与えるパラメータを返します。
     * 
     * @return メソッド実行時に与えるパラメータ。nullを返すことはありません。
     */
    Object[] getParameters();

    /**
     * 指定されたコンポーネントについて、メソッドを実行します。
     * <p>実行するメソッドは{@link #getMethod()}が返すメソッド、
     * 実行時の引数は{@link #getParameters()}が返すパラメータです。
     * 
     * @param component メソッド実行対象のコンポーネント。
     * @return メソッドの実行結果。
     * @throws WrappingRuntimeException メソッド実行に失敗した場合。
     */
    Object invoke(Object component) throws WrappingRuntimeException;

    /**
     * メソッドの実行結果の型を表すClassオブジェクトを返します。
     * 
     * @return メソッドの実行結果の型を表すClassオブジェクト。
     */
    Class<? extends Object> getReturnType();

    /**
     * メソッドの実行が可能かどうかを返します。
     * <p>このメソッドの返り値がfalseの場合は{@link #invoke(Object)}を呼び出してはいけません。
     * </p>
     * 
     * @return メソッドの実行が可能かどうか。
     */
    boolean shouldInvoke();
}
