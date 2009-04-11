package org.seasar.ymir;

import java.util.Map;

import org.seasar.ymir.interceptor.YmirProcessInterceptor;

/**
 * サーブレットの処理フェーズを表すインタフェースです。
 * <p>Dispatchは1回のHTTPリクエスト中で1回以上行なわれるサーブレットの処理フェーズです。
 * Dispatchは{@link Dispatcher}のいずれかの値に対応します。
 * </p>
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要はありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface Dispatch {
    /**
     * パスを返します。
     * <p>返されるパスはコンテキストパス相対です。</p>
     * <p>パスにはクエリ文字列は含まれません。</p>
     *
     * @return パス。
     */
    String getPath();

    /**
     * クエリ文字列を返します。
     * 
     * @return クエリ文字列。クエリ文字列が付与されていない場合はnullを返します。
     * @deprecated 代わりに{@link #getQueryParameterMap()}を使って下さい。
     */
    String getQueryString();

    /**
     * 絶対パスを返します。
     * <p>返されるパスはコンテキストパスとパスを連結したものです。</p>
     *
     * @return 絶対パス。
     */
    String getAbsolutePath();

    /**
     * ディスパッチャ名を返します。
     *
     * @return ディスパッチャ名。
     */
    Dispatcher getDispatcher();

    /**
     * パスに対応するPageコンポーネントの名前を返します。
     * <p>パスに対応するPageコンポーネントが存在しない場合はnullを返します。</p>
     *
     * @return Pageコンポーネント名。
     */
    String getPageComponentName();

    /**
     * パスに対応するPageコンポーネントを返します。
     * <p>パスに対応するPageコンポーネントが存在しない場合はnullを返します。</p>
     * 
     * @return Pageコンポーネント。
     */
    PageComponent getPageComponent();

    /**
     * リクエストを処理する元々のアクションを返します。
     * <p>このメソッドが返すアクションは、
     * リクエストされたパスに対応するアクションです。
     * 実際に実行されるアクションは{@link YmirProcessInterceptor}等によって
     * 変更されることがあるため、このメソッドが返すアクションが
     * 実際に実行されるアクションと一致するとは限りません。
     * <p>パスに対応するPageコンポーネントが存在しない場合はnullを返します。</p>
     * 
     * @return リクエストを処理する元々のアクション。
     * @see #getAction()
     * @see #getActionName()
     * @see #getOriginalActionName()
     * @since 1.0.3
     */
    Action getOriginalAction();

    /**
     * リクエストを処理する元々のアクションの名前を返します。
     * <p>このメソッドが返すアクションの名前は、
     * リクエストされたパスに対応するアクションの名前です。
     * 実際に実行されるアクションは{@link YmirProcessInterceptor}等によって
     * 変更されることがあるため、このメソッドが返すアクションの名前が
     * 実際に実行されるアクションの名前と一致するとは限りません。
     * <p>パスに対応するPageコンポーネントが存在しない場合はnullを返します。</p>
     * 
     * @return リクエストを処理する元々のアクションの名前。
     * @see #getAction()
     * @see #getActionName()
     * @see #getOriginalAction()
     * @since 1.0.3
     */
    String getOriginalActionName();

    /**
     * リクエストを処理する最終的なアクションを返します。
     * <p>このメソッドが返すアクションは、
     * {@link YmirProcessInterceptor}等によって
     * 変更された後の最終的なアクションです。
     * </p>
     * <p>パスに対応するPageコンポーネントが存在しない場合はnullを返します。</p>
     *
     * @return リクエストを処理する最終的なアクション。
     * @see #getActionName()
     * @see #getOriginalAction()
     * @see #getOriginalActionName()
     */
    Action getAction();

    /**
     * リクエストを処理する最終的なアクションの名前返します。
     * <p>このメソッドが返すアクションの名前は、
     * {@link YmirProcessInterceptor}等によって
     * 変更された後の最終的なアクションの名前です。
     * </p>
     * <p>パスに対応するPageコンポーネントが存在しない場合はnullを返します。</p>
     *
     * @return リクエストを処理する最終的なアクションの名前。
     * @see #getAction()
     * @see #getOriginalAction()
     * @see #getOriginalActionName()
     */
    String getActionName();

    /**
     * パスに連結されているpathInfo情報を返します。
     *
     * @return pathInfo情報。
     * パスにマッチしたPathMappingルールが持つpathInfoテンプレートがnullの場合はnullが返されます。
     */
    String getPathInfo();

    /**
     * パスから取り出したパラメータを格納しているMapを返します。
     * 
     * @return パラメータを格納しているMap。
     * パスにマッチしたPathMappingルールが持つパラメータテンプレートがnullの場合はnullが返されます。
     * @deprecated 代わりに{@link #getPathParameterMap()}を使用して下さい。
     */
    Map<String, String[]> getParameterMap();

    /**
     * パスから取り出したパラメータを格納しているMapを返します。
     * 
     * @return パラメータを格納しているMap。
     * パスにマッチしたPathMappingルールが持つパラメータテンプレートがnullの場合はnullが返されます。
     * @since 1.0.3
     */
    Map<String, String[]> getPathParameterMap();

    /**
     * パスに付与されているクエリパラメータを格納しているMapを返します。
     * <p>ファイルパラメータは対象外です。</p>
     *
     * @return クエリパラメータを格納しているMap。
     * @since 1.0.3
     */
    Map<String, String[]> getQueryParameterMap();

    /**
     * パスがパスマッピングにマッチしたかどうかを返します。
     *
     * @return パスがパスマッピングにマッチしたかどうか。
     */
    boolean isMatched();

    /**
     * パスへのリクエストを拒否すべきかどうかを返します。
     *
     * @return パスへのリクエストを拒否すべきかどうか。
     */
    boolean isDenied();

    /**
     * パスにマッチしたPathMappingオブジェクトを返します。
     * 
     * @return PathMappingオブジェクト。通常nullを返すことはありません。
     */
    MatchedPathMapping getMatchedPathMapping();
}
