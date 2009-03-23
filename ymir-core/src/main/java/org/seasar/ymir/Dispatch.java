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
     * @since 1.0.3
     */
    Action getOriginalAction();

    /**
     * リクエストを処理する最終的なアクションを返します。
     * <p>このメソッドが返すアクションは、
     * {@link YmirProcessInterceptor}等によって
     * 変更された後の最終的なアクションです。
     * </p>
     * <p>パスに対応するPageコンポーネントが存在しない場合はnullを返します。</p>
     *
     * @return リクエストを処理する最終的なアクション。
     * @see #getOriginalAction()
     */
    Action getAction();

    /**
     * パスとHTTPメソッドに対応するアクションの名前を返します。
     * <p>パスに対応するPageコンポーネントが存在しない場合はnullを返します。</p>
     *
     * @return アクション名。
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
     * パスから取り出したパラメータ情報を返します。
     * 
     * @return パラメータ情報。
     * パスにマッチしたPathMappingルールが持つパラメータテンプレートがnullの場合はnullが返されます。
     */
    Map<String, String[]> getParameterMap();

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
