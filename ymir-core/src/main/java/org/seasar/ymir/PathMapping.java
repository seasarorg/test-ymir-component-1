package org.seasar.ymir;

import java.util.regex.Pattern;

import org.seasar.kvasir.util.el.VariableResolver;

public interface PathMapping {

    String getActionNameTemplate();

    String getPageComponentNameTemplate();

    String getPathInfoTemplate();

    String getDefaultReturnValueTemplate();

    Object getDefaultReturnValue();

    Pattern getPattern();

    VariableResolver match(String path, String method);

    String getPageComponentName(VariableResolver resolver);

    String getActionName(VariableResolver resolver);

    String getPathInfo(VariableResolver resolver);

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

    boolean isDenied();

    /**
     * リクエストパラメータによるディスパッチを行なうかどうかを返します。
     * <p>このメソッドの返り値がtrueの場合、
     * コンポーネントが持つメソッドのうち、
     * アクション名とリクエストパラメータを
     * 「<code>_</code>」で連結したものと同じ名前のメソッドが呼び出されます。
     * 例えばコンポーネントのメソッドとして「<code>_post_update</code>」という名前のものと
     * 「<code>_post_replace</code>」という名前のものがある場合、
     * リクエストに対応するアクション名が「<code>_post</code>」でかつ
     * リクエストパラメータに「<code>update</code>」というものが含まれている場合は
     * 「<code>_post_update</code>」が呼び出されます。
     * （なお、「<code>_post_XXXX</code>」形式のメソッドが存在しない場合は
     * 「<code>_post</code>」メソッドが呼び出されます。）
     * </p>
     *
     * @return リクエストパラメータによるディスパッチを行なうかどうか。
     */
    boolean isDispatchingByParameter();

    Action getAction(PageComponent pageComponent, Request request,
            VariableResolver resolver);
}