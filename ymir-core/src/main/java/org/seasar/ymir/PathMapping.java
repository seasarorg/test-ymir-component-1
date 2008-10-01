package org.seasar.ymir;

import java.util.Map;

import org.seasar.kvasir.util.el.VariableResolver;

/**
 * リクエストパスとPageコンポーネントの関連付けを表すインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @see MatchedPathMapping
 */
public interface PathMapping {
    /**
     * 指定されたパス及びHTTPメソッドとのマッチングを行ないます。
     * <p>パスがこのオブジェクトが持つパターンとマッチした場合は、
     * マッチング結果の情報を持つ{@link VariableResolver}オブジェクトを返します。
     * マッチしなかった場合はnullを返します。
     * </p>
     * 
     * @param path パス文字列。
     * @param method HTTPメソッド。
     * @return パス中から取り出したパラメータを持つ{@link VariableResolver}オブジェクト。
     */
    VariableResolver match(String path, HttpMethod method);

    /**
     * パスに対応するPageコンポーネントの名前を返します。
     * 
     * @param resolver {@link #match(String, String)}が返す{@link VariableResolver}オブジェクト。
     * @return Pageコンポーネントの名前。
     */
    String getPageComponentName(VariableResolver resolver);

    /**
     * パスが持つpathInfo文字列を返します。
     * 
     * @param resolver {@link #match(String, String)}が返す{@link VariableResolver}オブジェクト。
     * @return pathInfo文字列。nullを返すことがあります。
     */
    String getPathInfo(VariableResolver resolver);

    /**
     * パスから取り出したパラメータを返します。
     * 
     * @param resolver {@link #match(String, String)}が返す{@link VariableResolver}オブジェクト。
     * @return パラメータが格納されたMap。
     */
    Map<String, String[]> getParameterMap(VariableResolver resolver);

    /**
    * アクションのデフォルトの返り値を返します。
    * <p>リクエストを処理した結果構築されたResponseが「パススルー」タイプであった場合に
    * 処理を遷移させる先を表すオブジェクトを返します。</p>
    * <p>デフォルトの返り値がnullでない場合でかつ
    * リクエストを処理した結果構築されたResponseが「パススルー」タイプであった場合に、
    * ResponseConstructorを使ってこのデフォルトの返り値から構築したResponseオブジェクトが
    * 最終的なResponseオブジェクトとしてフレームワークによって利用されます。
    * <p>パスに対応するコンポーネントが存在しない場合や
    * PathMappingルールにデフォルトの返り値が設定されていない場合はnullを返します。</p>
    *
    * @param resolver パスとパターンとのマッチング結果を表すVariableResolver。
    * @return デフォルトの返り値。
    */
    Object getDefaultReturnValue(VariableResolver resolver);

    /**
     * このオブジェクトが持つパターンとマッチするパスへの直接アクセスが禁止されているかどうかを返します。
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
     * @param resolver {@link #match(String, String)}が返す{@link VariableResolver}オブジェクト。
     * @return Actionオブジェクト。nullを返すこともあります。
     */
    Action getAction(PageComponent pageComponent, Request request,
            VariableResolver resolver);

    /**
     * レンダアクションを表すActionオブジェクトを構築して返します。
     * アクションメソッドが見つからなかった場合はnullを返します。
     * 
     * @param pageComponent パスに対応するPageComponent。
     * @param request 現在のRequest。
     * @param resolver {@link #match(String, String)}が返す{@link VariableResolver}オブジェクト。
     * @return Actionオブジェクト。nullを返すこともあります。
     * @since 0.9.6
     * @see #getRenderActionName(VariableResolver)
     */
    Action getRenderAction(PageComponent pageComponent, Request request,
            VariableResolver resolver);
}