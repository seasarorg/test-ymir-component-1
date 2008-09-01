package org.seasar.ymir.scope.handler;

import org.seasar.ymir.scope.AttributeNotFoundRuntimeException;

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
     * @throws AttributeNotFoundRuntimeException 属性が必須である場合に属性が存在しなかった場合。
     */
    void injectTo(Object component, String actionName)
            throws AttributeNotFoundRuntimeException;

    /**
     * 指定されたコンポーネントから値を取り出してスコープにアウトジェクトします。
     * 
     * @param component コンポーネント。
     * @param actionName 現在のリクエストに対応するアクションの名前。
     */
    void outjectFrom(Object component, String actionName);
}
