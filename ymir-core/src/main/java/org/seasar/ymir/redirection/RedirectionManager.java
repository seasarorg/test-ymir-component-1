package org.seasar.ymir.redirection;

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
    boolean isAddScopeIdAsRequestParameter();

    String getScopeIdKey();

    void removeScopeMap();

    void removeScopeMap(String scopeId);

    String getScopeId();

    String getScopeIdFromRequest();

    <T> T getScopeAttribute(String name);

    void setScopeAttribute(String name, Object value);

    void removeScopeAttribute(String name);

    boolean existsScopeMap();
}
