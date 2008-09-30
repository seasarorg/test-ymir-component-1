package org.seasar.ymir;

import java.lang.reflect.Method;

import org.seasar.ymir.scope.handler.ScopeAttributeResolver;
import org.seasar.ymir.scope.handler.ScopeAttributeInjector;
import org.seasar.ymir.scope.handler.ScopeAttributeOutjector;
import org.seasar.ymir.scope.handler.ScopeAttributePopulator;

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
     * スコープから値をポピュレートする必要のある属性のためのハンドラを返します。
     * <p>フレームワークはこのメソッドが返す属性の値をスコープから取り出して、
     * Pageオブジェクトの対応するプロパティにポピュレートします。
     * </p>
     * 
     * @return スコープから値をポピュレートする必要のある属性のためのハンドラ。nullを返すことはありません。
     * @see PageProcessor#populateScopeAttributes(Object, PageMetaData, String)
     */
    ScopeAttributePopulator[] getScopeAttributePopulators();

    /**
     * スコープから値をインジェクトする必要のある属性のためのハンドラを返します。
     * <p>フレームワークはこのメソッドが返す属性の値をスコープから取り出して、
     * Pageオブジェクトの対応するプロパティにインジェクトします。
     * </p>
     * 
     * @return スコープから値をインジェクトする必要のある属性のためのハンドラ。nullを返すことはありません。
     * @see PageProcessor#injectScopeAttributes(Object, PageMetaData, String)
     */
    ScopeAttributeInjector[] getScopeAttributeInjectors();

    /**
     * スコープに値をアウトジェクトする必要のある属性のためのハンドラを返します。
     * <p>フレームワークはこのメソッドが返す属性の値をPageオブジェクトから取り出して、
     * 対応するスコープにアウトジェクトします。
     * </p>
     * 
     * @return スコープに値をアウトジェクトする必要のある属性のためのハンドラ。nullを返すことはありません。
     * @see PageProcessor#outjectScopeAttributes(Object, PageMetaData, String)
     */
    ScopeAttributeOutjector[] getScopeAttributeOutjectors();

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

    /**
     * 指定されたメソッドの引数についてのリゾルバを返します。
     * <p>返される配列の大きさはメソッドの引数の個数と同じです。
     * </p>
     * <p>リゾルバがバインドされていない引数については、
     * 配列の対応する要素はnullになっています。
     * </p>
     * 
     * @param method メソッド。
     * @return リゾルバの配列。
     * @throws MethodNotFoundRuntimeException 指定されたメソッドがこのメタデータに対応するクラスのメソッドではない場合。
     */
    ScopeAttributeResolver[] getScopeAttributeResolversForParameters(
            Method method) throws MethodNotFoundRuntimeException;
}
