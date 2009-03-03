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
     * 現在のリクエストに指定されたRedirectionScopeのIDを返します。
     * <p>このメソッドが返すのは、次のリクエストに引き継ぐためのRedirectionScopeのIDではなく、
     * 前のリクエストから引き継がれたRedirectionScopeのIDです。
     * </p>
     * <p>前のリクエストからRedirectionScopeが引き継がれていない場合はnullを返します。
     * </p>
     * 
     * @return 現在のリクエストに指定されたRedirectionScopeのID。
     * @see #getScopeIdForNextRequest()
     */
    String getScopeId();

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
     * <p>valueとしてnullが設定された場合は属性を削除します。
     * </p>
     * 
     * @param name 属性名。
     * @param value 属性値。
     */
    void setScopeAttributeForNextRequest(String name, Object value);

    /**
     * 前のリクエストから引き継がれたRedirectionScopeが持つ全ての属性の名前のIteratorを返します。
     * <p>前のリクエストからRedirectionScopeが引き継がれていない場合は空のIteratorを返します。
     * </p>
     * 
     * @return 前のリクエストから引き継がれたRedirectionScopeが持つ全ての属性の名前のIterator。
     */
    Iterator<String> getScopeAttributeNames();

    /**
     * 前のリクエストから引き継がれたRedirectionScopeを削除します。
     */
    void clearScopeAttributes();

    /**
     * RedirectionScopeを次のリクエストに引き継ぐためのIDをレスポンスに設定します。
     * <p>前のリクエストから引き継がれたRedirectionScopeはこの時点で削除されます。
     * </p>
     * <p>引き継ぐRedirectionScopeが空の場合はIDは設定されません。
     * </p>
     */
    void populateScopeId();
}
