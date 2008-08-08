package org.seasar.ymir.scope.handler;

/**
 * スコープに格納される属性を扱うためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @see PageMetaData
 * @author YOKOTA Takehiko
 */
public interface ScopeAttributeHandler {
    /**
     * 指定されたコンポーネントに属性値をインジェクトします。
     * 
     * @param component コンポーネント。
     */
    void injectTo(Object component);

    /**
     * 指定されたコンポーネントから値を取り出してスコープにアウトジェクトします。
     * 
     * @param component コンポーネント。
     */
    void outjectFrom(Object component);

    /**
     * このオブジェクトの表す属性が、指定されたアクションにおいて有効かどうかを返します。
     * 
     * @param actionName アクション名。
     * @return 指定されたアクションにおいてこの属性が有効かどうか。
     */
    boolean isEnabled(String actionName);
}
