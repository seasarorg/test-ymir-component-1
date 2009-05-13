package org.seasar.ymir.extension.creator.mapping;

import org.seasar.kvasir.util.el.VariableResolver;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.PathMapping;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.MethodDesc;

public interface PathMappingExtraData<P extends PathMapping> {
    /**
     * 対応する{@link PathMapping}の実装クラスを返します。
     * 
     * @return 対応する{@link PathMapping}の実装クラス。
     */
    Class<P> getPathMappingClass();

    /**
     * 指定されたパスとHTTPメソッドに対応するPageクラスのアクションメソッドを表す{@link MethodDesc}
     * オブジェクトを構築して返します。
     * <p>formのボタンが押された時にボタン名に対応するアクション呼び出すケースなど、
     * 呼び出されるアクションがパスとHTTPメソッドだけでは決定できない場合があります。
     * このためPathMappingから正しくアクションを決定するためには正しいRequestオブジェクトが必要ですが、
     * YmirがHTMLテンプレートを解析してPageクラスを生成する際には実際にリクエストを送らないため、
     * 正しくアクションを決定することができません。
     * </p>
     * <p><code>seed</code>はそれを補うために指定されます。
     * このメソッドは<code>seed</code>に指定された情報を必要に応じて使用して正しいアクションを決定して
     * 対応する{@link MethodDesc}オブジェクトを構築します。
     * </p>
     * 
     * @param pool DescPoolオブジェクト。
     * nullを指定してはいけません。
     * 通常はアクションメソッドが属するクラスが属するDescPoolを指定します。
     * @param pathMapping リクエストパスとマッチした{@link PathMapping}。
     * @param resolver リクエストパスとPathMappingをマッチさせた結果を表す{@link VariableResolver}。
     * @param path リクエストのコンテキスト相対パス。末尾に「/」がついていても構いません。
     * @param method HTTPメソッド。
     * @param seed アクションを決定するための元となる情報。
     * @return 構築した{@link MethodDesc}オブジェクト。
     */
    MethodDesc newActionMethodDesc(DescPool pool, P pathMapping,
            VariableResolver resolver, String path, HttpMethod method,
            ActionSelectorSeed seed);

    /**
     * 指定されたパスとHTTPメソッドに対応するPageクラスのプリレンダメソッドを表す{@link MethodDesc}
     * オブジェクトを構築して返します。
     * 
     * @param pool DescPoolオブジェクト。
     * nullを指定してはいけません。
     * 通常はアクションメソッドが属するクラスが属するDescPoolを指定します。
     * @param pathMapping リクエストパスとマッチした{@link PathMapping}。
     * @param resolver リクエストパスとPathMappingをマッチさせた結果を表す{@link VariableResolver}。
     * @param path リクエストのコンテキスト相対パス。末尾に「/」がついていても構いません。
     * @param method HTTPメソッド。
     * @param seed アクションを決定するための元となる情報。
     * @return 構築した{@link MethodDesc}オブジェクト。
     * @see #newActionMethodDesc(PathMapping, VariableResolver, String, String, ActionSelectorSeed)
     */
    MethodDesc newRenderActionMethodDesc(DescPool pool, P pathMapping,
            VariableResolver resolver, String path, HttpMethod method,
            ActionSelectorSeed seed);
}
