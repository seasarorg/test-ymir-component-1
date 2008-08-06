package org.seasar.ymir.window;

import org.seasar.ymir.window.impl.WindowScope;

/**
 * Windowに関する操作を提供するためのインタフェースです。
 * <p>WindowはWebブラウザの画面を表す概念です。
 * 別ウィンドウを開いたりフレームを使ったりする場合など、アプリケーションが複数画面から構成される場合は
 * セッションによるクライアント情報の管理が難しくなります。
 * Windowの概念を使うことで、複数リクエストをまたがって存在するクライアント情報を画面単位に容易に扱うことができます。
 * </p>
 * <p>複数リクエストをまたがって存在するクライアント情報は{@link WindowScope}によって管理されます。
 * 従って、アプリケーション開発者は通常このインタフェースではなく{@link WindowScope}を使うことになります。
 * </p>
 * <p>別ウィンドウを開いたりフレームを使ったりするアプリケーションでWindowScopeを使う場合は、
 * 必ずWindow IDを画面毎に付与してリクエストに含めるようにして下さい。
 * リクエストに含める際のリクエストパラメータ名は{@link #getWindowIdKey()}が返す文字列である必要があります。
 * WindowScopeは非同期に操作されることを想定していないため、
 * リクエストにWindow IDが含まれない場合にWindowScopeを操作すると、
 * デフォルトのWindowのScopeが非同期に操作されて予期しない不具合が生じる危険性があります。
 * </p>
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 *
 * @see WindowScope
 * @author YOKOTA Takehiko
 */
public interface WindowManager {
    String getWindowIdKey();

    <T> T getScopeAttribute(String name);

    void setScopeAttribute(String name, Object value);

    void removeScopeAttribute(String name);

    String findWindowIdFromRequest();

    String getWindowIdFromRequest();

    void addStraddlingAttributeNamePattern(String namePattern);
}
