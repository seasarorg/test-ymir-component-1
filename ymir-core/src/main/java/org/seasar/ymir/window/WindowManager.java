package org.seasar.ymir.window;

import java.util.Iterator;

import org.seasar.ymir.Globals;
import org.seasar.ymir.Response;
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
 * </p>
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 *
 * @see WindowScope
 * @author YOKOTA Takehiko
 */
public interface WindowManager {
    /**
     * Windowに関するアプリケーションプロパティのキーの接頭辞です。
     */
    String APPKEYPREFIX_CORE_WINDOW = Globals.APPKEYPREFIX_CORE + "window.";

    /**
     * Window IDをリクエストパラメータとして受け取るためのキーを指定するためのプロパティのキーです。
     */
    String APPKEY_CORE_WINDOW_KEY = APPKEYPREFIX_CORE_WINDOW + "key";

    /**
     * Window IDのキーのデフォルト値です。
     */
    String DEFAULT_CORE_WINDOW_KEY = Globals.IDPREFIX + "window";

    String getWindowIdKey();

    <T> T getScopeAttribute(String name);

    void setScopeAttribute(String name, Object value);

    Iterator<String> getScopeAttributeNames();

    <T> T getScopeAttribute(String windowId, String name);

    void setScopeAttribute(String windowId, String name, Object value);

    Iterator<String> getScopeAttributeNames(String windowId);

    /**
     * 現在のWindowスコープにバインドされている属性のうち、
     * 指定されている名前の属性の値を返します。
     * <p>{@link #getScopeAttribute(String)}とは異なり、
     * Hotdeployモードの時でも補正せずにそのままオブジェクトを返します。
     * 従って、HotdeployモードではClassCastExceptionがスローされることがあります。
     * 通常は{@link #getScopeAttribute(String)}を使って下さい。
     * </p>
     * 
     * @param name 属性の名前。
     * @return 属性の値。存在しない場合はnullを返します。
     * @since 1.0.5
     */
    <T> T getRawScopeAttribute(String name);

    /**
     * 指定されたWindowスコープにバインドされている属性のうち、
     * 指定されている名前の属性の値を返します。
     * <p>{@link #getScopeAttribute(String, String)}とは異なり、
     * Hotdeployモードの時でも補正せずにそのままオブジェクトを返します。
     * 従って、HotdeployモードではClassCastExceptionがスローされることがあります。
     * 通常は{@link #getScopeAttribute(String, String)}を使って下さい。
     * </p>
     * 
     * @param windowId WindowスコープのID。
     * @param name 属性の名前。
     * @return 属性の値。存在しない場合はnullを返します。
     * @since 1.0.5
     */
    <T> T getRawScopeAttribute(String windowId, String name);

    /**
     * @since 1.0.3
     */
    boolean existsWindowScope();

    /**
     * @since 1.0.3
     */
    boolean existsWindowScope(String windowId);

    String findWindowId();

    String getWindowId();

    String findWindowIdForNextRequest();

    String getWindowIdForNextRequest();

    void addStraddlingAttributeNamePattern(String namePattern);
}
