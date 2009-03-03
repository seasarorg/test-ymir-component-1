package org.seasar.ymir.redirection;

import org.seasar.ymir.Request;
import org.seasar.ymir.Response;

/**
 * RedirectionScopeのIDを管理するためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.2
 */
public interface ScopeIdManager {
    /**
     * 現在のリクエストのための値を取得するRedirectionScopeのIDを返します。
     * <p>このメソッドが返すのは、次のリクエストに引き継ぐRedirectionScopeのIDではなく、
     * 前のリクエストから引き継がれたRedirectionScopeのIDです。
     * </p>
     * 
     * @return 現在のリクエストのための値を取得するRedirectionScopeのID。
     * RedirectionScopeが引き継がれていない場合はnullを返します。
     */
    String getScopeId();

    /**
     * RedirectionScopeを引き継ぐためのIDをレスポンスに設定します。
     * <p>このメソッドはレスポンスが確定してから呼び出されることが保証されていますので、
     * このメソッド内でコンテナからResponseオブジェクトを取り出して状態を変更することができます。
     * </p>
     * <p>IDをレスポンスに設定できない場合などはnullを返すようにして下さい。
     * </p>
     * 
     * @param scopeExists 引き継ぐべきRedirectionScopeが存在する（空でない）かどうか。
     * @return レスポンスに設定したID。
     */
    String populateScopeId(boolean scopeExists);
}
