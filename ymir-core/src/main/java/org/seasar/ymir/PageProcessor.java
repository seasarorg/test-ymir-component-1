package org.seasar.ymir;

import java.util.Map;

import org.seasar.ymir.ComponentMetaData;

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
     * 指定されたPageオブジェクトのプロパティに値をインジェクトします。
     * <p>インジェクトする値としては実際はリクエストパラメータなどが指定されます。
     * </p>
     * <p>インジェクトから保護されているプロパティには値をインジェクトしません。
     * </p>
     * <p><b>[注意]</b> このメソッドはYmir1.0.x系では削除される予定です。
     * </p>
     * 
     * @param page Pageオブジェクト。
     * @param metaData Pageオブジェクトに関するメタデータ。
     * @param properties インジェクトする値が格納されているMap。
     * @see ComponentMetaData#isProtected(String)
     */
    @Deprecated
    void injectProperties(Object page, ComponentMetaData metaData,
            Map<String, String[]> properties);

    /**
     * 指定されたPageオブジェクトのFormFile型のプロパティに値をインジェクトします。
     * <p>インジェクトする値としては実際はFormFile型のリクエストパラメータなどが指定されます。
     * </p>
     * <p>インジェクトから保護されているプロパティには値をインジェクトしません。
     * </p>
     * <p><b>[注意]</b> このメソッドはYmir1.0.x系では削除される予定です。
     * </p>
     * 
     * @param page Pageオブジェクト。
     * @param metaData Pageオブジェクトに関するメタデータ。
     * @param properties インジェクトする値が格納されているMap。
     * @see ComponentMetaData#isProtected(String)
     */
    @Deprecated
    void injectFormFileProperties(Object page, ComponentMetaData metaData,
            Map<String, FormFile[]> properties);

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
     * @see ComponentMetaData#getInjectedScopeAttributeHandlers()
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
