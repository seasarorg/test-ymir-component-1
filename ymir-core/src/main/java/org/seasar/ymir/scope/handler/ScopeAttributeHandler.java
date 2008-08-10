package org.seasar.ymir.scope.handler;

/**
 * スコープに格納される属性を扱うためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @see ComponentMetaData
 * @author YOKOTA Takehiko
 */
public interface ScopeAttributeHandler {
    /**
     * 指定されたコンポーネントに属性値をインジェクトします。
     * 
     * @param component コンポーネント。
     * @param actionName 現在のリクエストに対応するアクションの名前。
     */
    void injectTo(Object component, String actionName);

    /**
     * 指定されたコンポーネントから値を取り出してスコープにアウトジェクトします。
     * 
     * @param component コンポーネント。
     * @param actionName 現在のリクエストに対応するアクションの名前。
     */
    void outjectFrom(Object component, String actionName);
}
