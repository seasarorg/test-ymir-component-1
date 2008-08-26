package org.seasar.ymir.redirection;

import java.util.Iterator;

import org.seasar.ymir.redirection.impl.RedirectionInterceptor;

/**
 * RedirectionScopeに関する操作を提供するためのインタフェースです。
 * <p>通常はRedirectionScopeに関してアプリケーションで意識する必要はありませんが、
 * RedirectionScopeに関する情報をHTTPリクエストをまたいで受け渡すための方式等を
 * 変更したい場合は{@link RedirectionInterceptor}の実装クラスを作成して
 * その中でこのインタフェースを使ってRedirectionScope関連の操作を行ないます。
 * </p>
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 *
 * @see RedirectionInterceptor
 * @author YOKOTA Takehiko
 */
public interface RedirectionManager {
    /**
     * RedirectionScopeのIDをリクエストパラメータとして受け渡しするかどうかを返します。
     * <p>このメソッドがtrueを返す場合、RedirectionScopeのIDはリクエストパラメータとしてリダイレクト先の画面に渡されます。
     * falseを返す場合、IDはCookieの値としてリダイレクト先の画面に渡されます。
     * </p>
     * 
     * @return RedirectionScopeのIDをリクエストパラメータとして受け渡しするかどうか。
     */
    boolean isAddScopeIdAsRequestParameter();

    /**
     * RedirectionScopeのIDを保持しているリクエストパラメータの名前返します。
     * 
     * @return RedirectionScopeのIDを保持しているリクエストパラメータの名前。
     */
    String getRequestParameterNameForScopeId();

    /**
     * RedirectionScopeのIDを保持しているCookieの名前を返します。
     * 
     * @return RedirectionScopeのIDを保持しているCookieの名前。
     */
    String getCookieNameForScopeId();

    /**
     * 現在のリクエストのためのRedirectionScopeを表すMapを削除します。
     */
    void removeScopeMap();

    /**
     * 指定されたIDのRedirectionScopeを表すMapを削除します。
     * 
     * @param scopeId ID。
     */
    void removeScopeMap(String scopeId);

    /**
     * 現在のリクエストのためのRedirectionScopeのIDを返します。
     * <p>このメソッドが返すのは、前のリクエストから引き継がれたRedirectionScopeのIDではなく、
     * 次のリクエストに引き継ぐためのRedirectionScopeのIDです。
     * </p>
     * 
     * @return 現在のリクエストのためのRedirectionScopeのID。
     * @see #getScopeIdFromRequest()
     */
    String getScopeId();

    /**
     * 現在のリクエストに指定されたRedirectionScopeのIDを返します。
     * <p>このメソッドが返すのは、次のリクエストに引き継ぐためのRedirectionScopeのIDではなく、
     * 前のリクエストから引き継がれたRedirectionScopeのIDです。
     * </p>
     * <p>前のリクエストからRedirectionScopeが引き継がれていない場合はnullを返します。
     * </p>
     * 
     * @return 現在のリクエストに指定されたRedirectionScopeのID。
     * @see #getScopeId()
     */
    String getScopeIdFromRequest();

    /**
     * 前のリクエストから引き継がれたRedirectionScopeから指定された名前の属性を取り出して返します。
     * <p>前のリクエストからRedirectionScopeが引き継がれていない場合や、
     * 指定された名前の属性が存在しない場合はnullを返します。
     * </p>
     * 
     * @param <T> 属性値の型。
     * @param name 属性名。
     * @return 属性値。
     */
    <T> T getScopeAttribute(String name);

    /**
     * 次のリクエストに引き継ぐためのRedirectionScopeに属性を設定します。
     * 
     * @param name 属性名。
     * @param value 属性値。
     */
    void setScopeAttribute(String name, Object value);

    /**
     * 次のリクエストに引き継ぐためのRedirectionScopeから属性を削除します。
     * 
     * @param name 属性名。
     */
    void removeScopeAttribute(String name);

    /**
     * 前のリクエストから引き継がれたRedirectionScopeが持つ全ての属性の名前のIteratorを返します。
     * <p>前のリクエストからRedirectionScopeが引き継がれていない場合は空のIteratorを返します。
     * </p>
     * 
     * @return 前のリクエストから引き継がれたRedirectionScopeが持つ全ての属性の名前のIterator。
     */
    Iterator<String> getScopeAttributeNames();

    /**
     * 次のリクエストに引き継ぐためのRedirectionScopeを生成済みかどうかを返します。
     * 
     * @return 次のリクエストに引き継ぐためのRedirectionScopeを生成済みかどうか。
     */
    boolean existsScopeMap();
}
