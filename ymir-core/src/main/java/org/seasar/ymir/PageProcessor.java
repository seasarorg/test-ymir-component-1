package org.seasar.ymir;

import java.util.Map;

import org.seasar.ymir.PageMetaData;

/**
 * Pageオブジェクトを処理するためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * -^\\--\\\\\
 * @author YOKOTA Takehiko
 */
public interface PageProcessor {
    /**
     * 指定されたPageオブジェクトのプロパティに値をインジェクトします。
     * <p>インジェクトする値としては実際はリクエストパラメータなどが指定されます。
     * </p>
     * <p>インジェクトから保護されているプロパティには値をインジェクトしません。
     * </p>
     * 
     * @param page Pageオブジェクト。
     * @param metaData Pageオブジェクトに関するメタデータ。
     * @param properties インジェクトする値が格納されているMap。
     * @see PageMetaData#isProtected(String)
     */
    void injectProperties(Object page, PageMetaData metaData,
            Map<String, String[]> properties);

    /**
     * 指定されたPageオブジェクトのFormFile型のプロパティに値をインジェクトします。
     * <p>インジェクトする値としては実際はFormFile型のリクエストパラメータなどが指定されます。
     * </p>
     * <p>インジェクトから保護されているプロパティには値をインジェクトしません。
     * </p>
     * 
     * @param page Pageオブジェクト。
     * @param metaData Pageオブジェクトに関するメタデータ。
     * @param properties インジェクトする値が格納されているMap。
     * @see PageMetaData#isProtected(String)
     */
    void injectFormFileProperties(Object page, PageMetaData metaData,
            Map<String, FormFile[]> properties);

    /**
     * スコープに格納されている属性の値をPageオブジェクトのプロパティにインジェクトします。
     * 
     * @param page Pageオブジェクト。
     * @param metaData Pageオブジェクトに関するメタデータ。
     * @param actionName 実行するアクションの名前。
     * @see PageMetaData#getInjectedScopeAttributeHandlers()
     */
    void injectScopeAttributes(Object page, PageMetaData metaData,
            String actionName);

    /**
     * スコープに対してPageオブジェクトのプロパティから値をアウトジェクトします。
     * 
     * @param page Pageオブジェクト。
     * @param metaData Pageオブジェクトに関するメタデータ。
     * @param actionName 実行されたアクションの名前。
     * @see PageMetaData#getOutjectedScopeAttributeHandlers()
     */
    void outjectScopeAttributes(Object page, PageMetaData metaData,
            String actionName);

    /**
     * Pageオブジェクトについて、指定されたフェーズに関連付けられているメソッドを実行します。
     * 
     * @param page Pageオブジェクト。
     * @param metaData Pageオブジェクトに関するメタデータ。
     * @param phase フェーズ。
     * @see PageMetaData#getMethods(Phase)
     */
    void invokeMethods(Object page, PageMetaData metaData, Phase phase);
}
