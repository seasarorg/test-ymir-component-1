package org.seasar.ymir;

import java.lang.reflect.Method;

import org.seasar.ymir.scope.handler.ScopeAttributeHandler;

/**
 * コンポーネントに関する情報を表すインタフェースです。
 * <p>主にコンポーネントが持つ属性やメソッドなどを表します。
 * </p>
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはオブジェクトの内部状態を変えない操作に関してスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface ComponentMetaData {
    /**
     * 指定された名前のプロパティがインジェクトから保護されているかどうかを返します。
     * <p>Ymirではセキュリティ上、任意のプロパティにリクエストパラメータ等の値が自動的にインジェクトされるようにしていません。
     * このメソッドがtrueを返すプロパティにはフレームワークが値をインジェクトしません。
     * </p>
     * <p><b>[注意]</b> このメソッドはYmir1.0.x系では削除される予定です。
     * </p>
     * 
     * @param propertyName プロパティ名。
     * @return インジェクトから保護されているかどうか。
     * @see PageProcessor#injectProperties(Object, PageMetaData, java.util.Map)
     * @see PageProcessor#injectFormFileProperties(Object, PageMetaData, java.util.Map)
     */
    boolean isProtected(String propertyName);

    /**
     * スコープから値をポピュレートする必要のある属性のためのハンドラを返します。
     * <p>フレームワークはこのメソッドが返す属性の値をスコープから取り出して、
     * Pageオブジェクトの対応するプロパティにポピュレートします。
     * </p>
     * 
     * @return スコープから値をポピュレートする必要のある属性のためのハンドラ。nullを返すことはありません。
     * @see PageProcessor#populateScopeAttributes(Object, PageMetaData, String)
     */
    ScopeAttributeHandler[] getPopulatedScopeAttributeHandlers();

    /**
     * スコープから値をインジェクトする必要のある属性のためのハンドラを返します。
     * <p>フレームワークはこのメソッドが返す属性の値をスコープから取り出して、
     * Pageオブジェクトの対応するプロパティにインジェクトします。
     * </p>
     * 
     * @return スコープから値をインジェクトする必要のある属性のためのハンドラ。nullを返すことはありません。
     * @see PageProcessor#injectScopeAttributes(Object, PageMetaData, String)
     */
    ScopeAttributeHandler[] getInjectedScopeAttributeHandlers();

    /**
     * スコープに値をアウトジェクトする必要のある属性のためのハンドラを返します。
     * <p>フレームワークはこのメソッドが返す属性の値をPageオブジェクトから取り出して、
     * 対応するスコープにアウトジェクトします。
     * </p>
     * 
     * @return スコープに値をアウトジェクトする必要のある属性のためのハンドラ。nullを返すことはありません。
     * @see PageProcessor#outjectScopeAttributes(Object, PageMetaData, String)
     */
    ScopeAttributeHandler[] getOutjectedScopeAttributeHandlers();

    /**
     * 指定されたフェーズに関連付けられているメソッドを返します。
     * <p>フレームワークは、フェーズに関連付けられているPageオブジェクトのメソッドを呼び出します。
     * このメソッドは呼び出すメソッドを取得するためにフレームワークから呼び出されます。
     * </p>
     * 
     * @param phase フェーズ。
     * @return メソッド。nullを返すことはありません。
     * @see PageProcessor#invokeMethods(Object, PageMetaData, Phase)
     */
    Method[] getMethods(Phase phase);
}
