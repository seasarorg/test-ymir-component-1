package org.seasar.ymir.session;

import java.util.Iterator;

import javax.servlet.http.HttpSession;

/**
 * HTTPセッションを管理するためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface SessionManager {
    /**
     * 現在のセッションを表すHttpSessionオブジェクトを返します。
     * <p>セッションが存在しない場合はセッションを作成して返します。
     * </p>
     * <p>このメソッドはHttpServletRequest#getSession()と同じです。
     * </p>
     * 
     * @return HttpSessionオブジェクト。
     */
    HttpSession getSession();

    /**
     * 現在のセッションを表すHttpSessionオブジェクトを返します。
     * <p>セッションが存在しない場合は、
     * <code>create</code>がtrueである場合はセッションを作成して返します。
     * そうでない場合はnullを返します。
     * </p>
     * <p>このメソッドはHttpServletRequest#getSession(boolean)と同じです。
     * </p>
     * 
     * @return HttpSessionオブジェクト。
     */
    HttpSession getSession(boolean create);

    /**
     * 現在のセッションを無効化します。
     * <p>現在セッションが存在すれば、セッションを無効化します。
     * 存在しない場合は何もしません。
     * </p>
     * <p>このメソッドは単にHttpSession#invalidate()を呼び出すのと同じです。
     * </p>
     */
    void invalidateSession();

    /**
     * 現在のセッションを無効化した上で新しいセッションを作成します。
     * <p>現在セッションが存在すれば、現在のセッションを無効化した上でセッションを新たに作成して返します。
     * 現在セッションが存在しなければ、単に新しいセッションを作成して返します。
     * </p>
     * <p>単にHttpSession#invalidate()を呼び出すのと異なり、
     * このメソッドではフレームワークがセッションに保持している
     * 「セッションを跨ぐ」べき属性が新たなセッションに引き継がれます。
     * 従って、Ymirアプリケーションでセッションを無効化して新たなセッションを作成したい場合は、
     * 通常このメソッドを使ってセッションを再作成すべきです。
     * </p>
     * 
     * @return 作成されたセッションオブジェクト。
     */
    HttpSession invalidateAndCreateSession();

    /**
     * セッションを無効化して再作成する場合にセッションを跨いで値を保持したい属性の名前のパターンを追加します。
     * 
     * @param namePattern 属性の名前のパターン。
     * @see #isStraddlingAttributeName(String)
     */
    void addStraddlingAttributeNamePattern(String namePattern);

    /**
     * 現在のセッションにバインドされている属性のうち、
     * 指定されている名前の属性の値を返します。
     * 
     * @param name 属性の名前。
     * @return 属性の値。存在しない場合はnullを返します。
     */
    Object getAttribute(String name);

    /**
     * 指定された名前と値を持つ属性を現在のセッションにバインドします。
     * 
     * @param name 属性の名前。
     * @param value 属性の値。nullを指定すると属性が削除されます。
     */
    void setAttribute(String name, Object value);

    /**
     * 現在のセッションから指定された名前の属性を削除します。
     * 
     * @param name 属性の名前。
     */
    void removeAttribute(String name);

    /**
     * 現在のセッションにバインドされている全ての属性の名前のIteratorを返します。
     * 
     * @return Iteratorオブジェクト。
     */
    Iterator<String> getAttributeNames();

    /**
     * セッションが存在するかどうかを返します。
     * 
     * @return セッションが存在するかどうか。
     */
    boolean isSessionPresent();

    /**
     * 指定された名前の属性の値が外部で変更されたかもしれないことを通知します。
     * <p>クラスタリング環境においてセッションレプリケーションが行なわれている場合、
     * 属性の値であるオブジェクトを外部で変更した場合は
     * そのことをサーブレットコンテナに通知する必要があります。
     * このメソッドを呼び出すことで、指定された値の属性の値が外部で変更されたかもしれないことを
     * フレームワークに通知することができます。
     * </p>
     * <p>このメソッドはフレームワークによって使用されます。
     * 通常アプリケーションコードからこのメソッドを呼び出す必要はありません。
     * </p>
     * 
     * @param name 属性の名前。
     */
    void refreshAttribute(String name);
}
