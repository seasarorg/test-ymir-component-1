package org.seasar.ymir;

import java.util.Map;

import org.seasar.kvasir.util.el.VariableResolver;

/**
 * リクエストパスに実際にマッチした{@link PathMapping}に関する情報を扱うためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要はありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @see PathMapping
 */
public interface MatchedPathMapping {
    /**
     * パスにマッチしたPathMappingオブジェクトを返します。
     * @return PathMappingオブジェクト。
     */
    PathMapping getPathMapping();

    /**
     * PathMappingとマッチさせた結果パスから取り出した各パラメータを保持する
     * VariableResolverオブジェクトを返します。
     * 
     * @return VariableResolverオブジェクト。
     */
    VariableResolver getVariableResolver();

    /**
     * パスに対応するPageコンポーネントの名前を返します。
     * 
     * @return パスに対応するPageコンポーネントの名前。
     */
    String getPageComponentName();

    /**
     * パスから取り出したpathInfo情報を返します。
     * 
     * @return パスから取り出したpathInfo情報。
     */
    String getPathInfo();

    /**
     * パスに埋め込まれていたパラメータ情報を保持するMapオブジェクトを返します。
     * 
     * @return パスに埋め込まれていたパラメータ情報を保持するMapオブジェクト。
     */
    Map<String, String[]> getParameterMap();

    /**
     * アクションのデフォルトの返り値を返します。
     * 
     * @return アクションのデフォルトの返り値。
     */
    Object getDefaultReturnValue();

    /**
     * パスへの直接アクセスが禁止されているかどうかを返します。
     * 
     * @return パスへの直接アクセスが禁止されているかどうか。
     */
    boolean isDenied();

    /**
     * 実行すべきアクションを表すActionオブジェクトを構築して返します。
     * 実行すべきアクションメソッドが見つからなかった場合はnullを返します。
     * 
     * @param pageComponent パスに対応するPageComponent。
     * @param request 現在のRequest。
     * @return Actionオブジェクト。nullを返すこともあります。
     */
    Action getAction(PageComponent pageComponent, Request request);

    /**
     * プリレンダアクションを表すActionオブジェクトを構築して返します。
     * アクションメソッドが見つからなかった場合はnullを返します。
     * 
     * @param pageComponent パスに対応するPageComponent。
     * @param request 現在のRequest。
     * @return Actionオブジェクト。nullを返すこともあります。
     * @since 1.0.0
     */
    Action getPrerenderAction(PageComponent pageComponent, Request request);
}
