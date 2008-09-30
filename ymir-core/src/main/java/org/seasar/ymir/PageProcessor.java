package org.seasar.ymir;

/**
 * Pageオブジェクトを処理するためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface PageProcessor {
    /**
     * スコープに格納されている属性の値をPageオブジェクトのプロパティにポピュレートします。
     * <p>子孫Pageについては処理は行なわれません。
     * </p>
     * 
     * @param pageComponent PageComponentオブジェクト。
     * @param actionName 実行するアクションの名前。
     */
    void populateScopeAttributes(PageComponent pageComponent, String actionName);

    /**
     * スコープに格納されている属性の値をPageオブジェクトのプロパティにインジェクトします。
     * <p>子孫Pageについては処理は行なわれません。
     * </p>
     * 
     * @param pageComponent PageComponentオブジェクト。
     * @param metaData Pageオブジェクトに関するメタデータ。
     * @param actionName 実行するアクションの名前。
     * @see ComponentMetaData#getScopeAttributeInjectors()
     */
    void injectScopeAttributes(PageComponent pageComponent, String actionName);

    /**
     * スコープに対してPageオブジェクトのプロパティから値をアウトジェクトします。
     * <p>子孫Pageについては処理は行なわれません。
     * </p>
     * 
     * @param pageComponent PageComponentオブジェクト。
     * @param metaData Pageオブジェクトに関するメタデータ。
     * @param actionName 実行されたアクションの名前。
     */
    void outjectScopeAttributes(PageComponent pageComponent, String actionName);

    /**
     * Pageオブジェクトについて、指定されたフェーズに関連付けられているメソッドを実行します。
     * <p>子孫Pageについては処理は行なわれません。
     * </p>
     * 
     * @param pageComponent PageComponentオブジェクト。
     * @param phase フェーズ。
     */
    void invokeMethods(PageComponent pageComponent, Phase phase);
}
