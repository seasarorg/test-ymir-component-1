package org.seasar.ymir.scope;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.seasar.ymir.PageComponent;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.converter.annotation.TypeConversionHint;

/**
 * スコープに関する操作を行なうためのインタフェースです。
 * 
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.0
 */
public interface ScopeManager {
    /**
     * 指定されたスコープから指定された名前とタイプに対応する属性の値を取り出して返します。
     * <p>このメソッドは<code>getAttribute(scope, name, type, null, required, convertNullToDefaultValueWhereTypeIsPrimitive)</code>
     * と同じです。
     * </p>
     * 
     * @param scope スコープ。
     * @param name 属性の名前。
     * @param type 属性のタイプ。
     * @param required 属性の値が存在する必要があるかどうか。
     * @param convertNullToDefaultValueWhereTypeIsPrimitive typeがプリミティブ型である場合、
     * 値が見つからなかったかnullの時にデフォルト値に変換するかどうか。
     * @return 属性の値。
     * @throws AttributeNotFoundRuntimeException requiredがtrueである場合で値が見つからなかったかnullであった時。
     */
    <T> T getAttribute(Scope scope, String name, Class<T> type,
            boolean required,
            boolean convertNullToDefaultValueWhereTypeIsPrimitive)
            throws AttributeNotFoundRuntimeException;

    /**
     * 指定されたスコープから指定された名前とタイプに対応する属性の値を取り出して返します。
     * <p>値は指定されたヒントに基づいて指定されたタイプに変換されて返されます。
     * </p>
     * <p>requiredがtrueである場合、値が見つからなかったかnullであった時はAttributeNotFoundRuntimeExceptionがスローされます。
     * </p>
     * <p>requiredがfalseかつconvertNullToDefaultValueWhereTypeIsPrimitiveがtrueかつ
     * typeがプリミティブ型である場合、値が見つからなかったかnullであった時はプリミティブ型に対応するデフォルト値を返します。
     * </p>
     * 
     * @param scope スコープ。
     * @param name 属性の名前。
     * @param type 属性のタイプ。
     * @param hint 変換のためのヒント。nullを指定することもできます。
     * ヒントとしては変換がメソッドなどを経由して行なわれる際に対象メソッドに付与されている
     * アノテーションのうち{@link TypeConversionHint}メタアノテーションが付与されているものが渡されます。
     * @param required 属性の値が存在する必要があるかどうか。
     * @param convertNullToDefaultValueWhereTypeIsPrimitive typeがプリミティブ型である場合、
     * 値が見つからなかったかnullの時にデフォルト値に変換するかどうか。
     * @return 属性の値。
     * @throws AttributeNotFoundRuntimeException requiredがtrueである場合で値が見つからなかったかnullであった時。
     * @see TypeConversionManager#convert(Object, Class, Annotation[])
     */
    <T> T getAttribute(Scope scope, String name, Class<T> type,
            Annotation[] hint, boolean required,
            boolean convertNullToDefaultValueWhereTypeIsPrimitive)
            throws AttributeNotFoundRuntimeException;

    /**
     * スコープに格納されている属性の値をPageオブジェクトのプロパティにポピュレートします。
     * <p>子孫Pageについては処理は行なわれません。
     * </p>
     * 
     * @param pageComponent PageComponentオブジェクト。
     * @param actionName 実行するアクションの名前。
     * @since 1.0.0
     */
    void populateScopeAttributes(PageComponent pageComponent, String actionName);

    /**
     * スコープに格納されている属性の値をPageオブジェクトのプロパティにインジェクトします。
     * <p>子孫Pageについては処理は行なわれません。
     * </p>
     * 
     * @param pageComponent PageComponentオブジェクト。
     * @param metaData Pageオブジェクトに関するメタデータ。
     * @param actionName 実行するアクションの名前。
     * @since 1.0.0
     */
    void injectScopeAttributes(PageComponent pageComponent, String actionName);

    /**
     * スコープに対してPageオブジェクトのプロパティから値をアウトジェクトします。
     * <p>子孫Pageについては処理は行なわれません。
     * </p>
     * 
     * @param pageComponent PageComponentオブジェクト。
     * @param metaData Pageオブジェクトに関するメタデータ。
     * @param actionName 実行されたアクションの名前。
     * @since 1.0.0
     */
    void outjectScopeAttributes(PageComponent pageComponent, String actionName);

    /**
     * 指定されたクラスの指定されたメソッドを実行するために必要な引数を解決します。
     * <p>指定されたクラスの指定されたメソッドを実行するために、
     * メソッドの引数毎にスコープから値を取り出して並べた配列を返します。
     * 引数に対応する値がスコープに存在しない場合は外部パラメータの値が順次利用されます。
     * 外部パラメータの値が不足している場合はnull（引数の型がプリミティブ型の場合はデフォルト値）が使われます。
     * </p>
     * 
     * @param pageClass ページクラス。nullを指定してはいけません。
     * @param method メソッド。pageClassのメソッドである必要があります。
     * nullを指定してはいけません。
     * @param extendedParams 外部パラメータ。
     * スコープから値を解決できなかった時に順次利用されます。
     * @return メソッドの引数の値の配列。メソッドの引数の個数と同じ長さの配列です。
     * nullを指定してはいけません。
     * @since 1.0.0
     */
    Object[] resolveParameters(Class<?> pageClass, Method method,
            Object[] extendedParams);

    /**
     * 指定されたパラメータを指定されたオブジェクトのプロパティにポピュレートします。
     * 
     * @param bean ポピュレート先のオブジェクト。
     * nullを指定した場合は何もされません。
     * @param parameterMap ポピュレートするパラメータのMap。
     * nullを指定した場合は何もされません。
     * @throws PopulationFailureException ポピュレートに失敗した場合。
     * @since 1.0.7
     */
    void populate(Object bean, Map<String, ?> parameterMap)
            throws PopulationFailureException;

    /**
     * 指定されたパラメータを指定されたオブジェクトのプロパティにポピュレートします。
     * <p>ポピュレートに失敗しても処理を継続します。
     * </p>
     * 
     * @param bean ポピュレート先のオブジェクト。
     * nullを指定した場合は何もされません。
     * @param parameterMap ポピュレートするパラメータのMap。
     * nullを指定した場合は何もされません。
     * @since 1.0.7
     */
    void populateQuietly(Object bean, Map<String, ?> parameterMap);

    /**
     * 指定された値を指定されたオブジェクトのプロパティにポピュレートします。
     * 
     * @param bean ポピュレート先のオブジェクト。
     * nullを指定した場合は何もされません。
     * @param name プロパティ名。
     * nullを指定してはいけません。
     * @param value 値。
     * nullを指定することもできます。
     * @throws PopulationFailureException ポピュレートに失敗した場合。
     * @since 1.0.7
     */
    void populate(Object bean, String name, Object value)
            throws PopulationFailureException;

    /**
     * 指定された値を指定されたオブジェクトのプロパティにポピュレートします。
     * <p>ポピュレートに失敗しても処理を継続します。
     * </p>
     * 
     * @param bean ポピュレート先のオブジェクト。
     * nullを指定した場合は何もされません。
     * @param name プロパティ名。
     * nullを指定してはいけません。
     * @param value 値。
     * nullを指定することもできます。
     * @since 1.0.7
     */
    void populateQuietly(Object bean, String name, Object value);
}
