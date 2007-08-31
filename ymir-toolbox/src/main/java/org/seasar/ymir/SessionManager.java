package org.seasar.ymir;

import javax.servlet.http.HttpSession;

/**
 * HTTPセッションを管理するためのインタフェースです。
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
     * セッションを無効化して再作成する場合にセッションを跨いで値を保持したい属性の名前の配列を返します。
     * 
     * @return 属性の名前の配列。nullが返ることはありません。
     */
    String[] getStraddlingAttributeNames();

    /**
     * セッションを無効化して再作成する場合にセッションを跨いで値を保持したい属性の名前を追加します。
     * 
     * @param attributeName 属性の名前。
     */
    void addStraddlingAttributeName(String attributeName);
}
